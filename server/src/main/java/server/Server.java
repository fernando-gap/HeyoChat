package server;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import server.contract.Database;
import server.contract.Protocol;
import server.contract.impl.DatabaseImpl;
import server.contract.impl.ProtocolImpl;
import shared.client.Message;
import shared.client.User;
import shared.communication.Client;
import shared.communication.Receiver;
import shared.communication.Response;
import shared.communication.Sender;
import shared.communication.Action;

class Server {
    private Protocol<Sender<?>, Receiver<?>, Socket> protocol;
    private Database database;
    private boolean isOperating = true;

    public Server(Protocol<Sender<?>, Receiver<?>, Socket> protocol, Database database) {
        this.protocol = protocol;
        this.database = database;
    }

    public void listen() {
        while (isOperating) {
            Sender<?> sender = protocol.receive();
            Receiver<?> receiver = doAction(sender);
        }
    }

    private Receiver<?> doAction(Sender<?> client) throws ClassCastException {
        Receiver<?> receiver = new Receiver<>(null, Action.NONE);
            switch (client.getAction()) {
                case CREATE_ACCOUNT:
                    receiver = createUserAccount(client);
                // case SEND_USER2USER:
                //     receiver = sendMessage2User(client);
                //     break;
                // case SEND_USER2GROUP:
                //     receiver = sendMessage2Group(client);
                case NONE:
                    return receiver;
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


    // private Boolean authorize(Connection sender) {
    //     return null;
    // }

    // private Receiver<Integer> sendMessage2User(Client client) {
    //     Sender<?> sender = (Sender<?>) client;
    //     return null;
    // }

    // private Receiver<Integer> sendMessage2Group(Client client) {
    //     return null;
    // }

    // private Receiver<Message> getUserMessages(Client client) {

    // }

    // private Receiver<Message> getGroupMessages(Client client) {

    // }

    // private Receiver<Integer> registerUser(Client client) {
    //     @SuppressWarnings("unchecked")
    //     Sender<User> sender = (Sender<User>) client;
    // }

    public static void main(String[] args) {
        try {
            DatabaseImpl db = new DatabaseImpl();
            ProtocolImpl protocol = new ProtocolImpl(5000);

            Server server = new Server(protocol, db);
            server.listen();
        } catch (SQLException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
