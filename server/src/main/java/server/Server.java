package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedDeque;

import server.contract.Database;
import server.contract.Protocol;
import server.contract.impl.DatabaseImpl;
import server.contract.impl.ProtocolImpl;
import shared.client.Message;
import shared.client.User;
import shared.communication.Receiver;
import shared.communication.Response;
import shared.communication.Sender;
import shared.communication.Action;

class Server {
    private ConcurrentLinkedDeque<Connection> readyDeque = new ConcurrentLinkedDeque<>();
    private Protocol<Connection> protocol;
    private Thread protocolT;
    private Database database;
    private ServerSocketChannel server;
    private boolean isRunning = false;

    public Server(Protocol<Connection> protocol, Database database) {
        protocol.setReadyDeque(readyDeque);
        this.protocol = protocol;
        this.database = database;
        protocolT = new Thread(protocol);
    }

    public void start(String host, int port) {
        listen(new InetSocketAddress(host, port));
        protocolT.start();
        isRunning = true;
        poll();
    }

    private void listen(InetSocketAddress address) {
        try {
            server = ServerSocketChannel.open();
            server.bind(address);
            protocol.setServer(server);
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }
    }

    private void poll() {
        while (isRunning) {
            if (!readyDeque.isEmpty()) {
                Connection connection = readyDeque.remove();

                /* the timeout waits the buffer to be filled given a delay */
                if (connection.hasTimeout()) {
                    System.out.println("[STATUS]: timeout accepted");
                    Sender<?> sender = process(connection);
                    if (sender != null) {
                        System.out.println("[STATUS]: Processing client...");
                        Receiver<?> receiver = doAction(sender);
                        connection.setReceiver(receiver);
                        send(connection);
                        connection.getReadBuf().clear();
                        System.out.println("[STATUS]: done processing.");
                    } else {
                        System.out.println("[ERROR]: object is incomplete");
                    }
                } else {
                    // System.out.println("[STATUS]: waiting timeout");
                    if (!readyDeque.contains(connection)) {
                        readyDeque.add(connection);
                    }
                }
            }
        }
    }

    private Sender<?> process(Connection client) {
        try {
            ByteBuffer buf = client.getReadBuf();
            ByteArrayInputStream bytes = new ByteArrayInputStream(buf.array());
            ObjectInputStream in = new ObjectInputStream(bytes);
            Sender<?> sender = (Sender<?>) in.readObject();
            in.close();
            return sender;
        } catch (IOException e) {
            System.err.println(e);
            return null;
        } catch (ClassNotFoundException e) {
            System.err.println(e);
            return null;
        }
    }

    public void send(Connection connection) {
        try {
            Receiver<?> receiver = connection.getReceiver();
            SocketChannel channel = connection.getChannel();
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
            objectOut.writeObject(receiver);
            objectOut.flush();
            ByteBuffer buf = connection.getWriteBuf(byteOut.toByteArray());
            while (buf.hasRemaining()) {
                channel.write(buf);
            }

            buf.clear();
            objectOut.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private Receiver<?> doAction(Sender<?> client) throws ClassCastException {
        Receiver<?> receiver = new Receiver<>(null, Action.NONE);
        switch (client.getAction()) {
            case CREATE_ACCOUNT:
                receiver = createUserAccount(client);
                break;
            case SIGN_IN:
                receiver = signInUserAccount(client);
                break;
            case ADD_USER_LIST:
                receiver = sendMessage2User(client);
                System.err.println(receiver.getErrorMessage() + " " + receiver.getResponse());
                if (receiver.getResponse() != Response.ERROR) {
                    Receiver<String> newReceiver = new Receiver<>(Action.ADD_USER_LIST);
                    newReceiver.appendData(client.getReceiverName());
                    return newReceiver;
                }
                break;
            // case SEND_USER2USER:
            // receiver = sendMessage2User(client);
            // break;
            // case SEND_USER2GROUP:
            // receiver = sendMessage2Group(client);
            case NONE:
                break;
        }

        return receiver;
    }

    private Receiver<String> createUserAccount(Sender<?> sender) {
        User newUser = (User) sender.getData(0);
        Receiver<String> response = new Receiver<String>(Action.CREATE_ACCOUNT, Response.ERROR);

        try {
            database.createUser(newUser.getName(), newUser.getPassword());
            response = new Receiver<>(Action.CREATE_ACCOUNT);
            response.appendData("ok");
        } catch (SQLException e) {
            System.err.println(e);
            response.appendData(e.toString());
        }
        return response;
    }

    private Receiver<User> signInUserAccount(Sender<?> sender) {
        User user = (User) sender.getData(0);
        Receiver<User> response = new Receiver<User>(Action.SIGN_IN, Response.ERROR);

        try {
            User userDatabase = database.getUser(user.getName());

            if (userDatabase != null) {
                if (user.getPassword().equals(userDatabase.getPassword())) {
                    response = new Receiver<User>(Action.SIGN_IN);
                    userDatabase.setPasswordNull();
                    response.appendData(userDatabase);
                } else {
                    response.setErrorMessage("Password does not match");
                }
            } else {
                response.setErrorMessage("User does not exist");
            }
        } catch (SQLException e) {
            System.err.println(e);
            response.setErrorMessage(e.toString());
        } catch (Error e) {
            System.err.println(e);
            response.setErrorMessage(e.toString());
        }
        return response;
    }

    private Receiver<Integer> sendMessage2User(Sender<?> sender) {
        Receiver<Integer> receiverResponse = new Receiver<>(Action.SEND_USER2USER, Response.ERROR);
        Message message = (Message) sender.getData(0);
        try {
            database.saveMessage(message, sender.getName(), sender.getReceiverName());
            Connection connection = protocol.getConnection(sender.getReceiverName());

            if (connection != null) {
                /* send from */
                receiverResponse.resetResponse();
                receiverResponse.appendData(1);

                /* send to */
                Receiver<Message> receiverMessage = new Receiver<>(Action.SEND_USER2USER);
                receiverMessage.appendData(message);
                connection.setReceiver(receiverMessage);
                send(connection);
            }
            return receiverResponse;
        } catch (SQLException e) {
            System.err.println(e);
            receiverResponse.setErrorMessage("Can't send message");
        }
        return receiverResponse;
    }

    // private Boolean authorize(Connection sender) {
    // return null;
    // }

    // private Receiver<Integer> sendMessage2Group(Client client) {
    // return null;
    // }

    // private Receiver<Message> getUserMessages(Client client) {

    // }

    // private Receiver<Message> getGroupMessages(Client client) {

    // }

    // private Receiver<Integer> registerUser(Client client) {
    // @SuppressWarnings("unchecked")
    // Sender<User> sender = (Sender<User>) client;
    // }

    public static void main(String[] args) {
        try {
            DatabaseImpl db = new DatabaseImpl();
            ProtocolImpl protocol = new ProtocolImpl();
            Server server = new Server(protocol, db);
            server.start("localhost", 5000);
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
}
