package server.contract.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


import server.contract.Protocol;
import shared.communication.Action;
import shared.communication.Sender;
import shared.communication.Receiver;



public class ProtocolImpl implements Protocol<Sender<?>, Receiver<?>, Socket> {
    private ServerSocket server;
    private HashMap<String, Socket> activeConnections = new HashMap<>();

    public ProtocolImpl(int port) throws IOException {
        server = new ServerSocket(port);
    }

    @Override
    public Sender<?> receive() {
        Sender<?> sender = new Sender<>(Action.NONE);

        try {
            Socket socket = server.accept();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            try {
                sender = (Sender<?>) in.readObject();
                activeConnections.put(sender.getName(), socket);
            } catch(ClassNotFoundException e) {
                System.err.println(e);
                System.exit(1);
            }
        } catch (IOException e) {
            return sender;
        }
        return sender;
    }

    @Override
    public void send(Receiver<?> client) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'send'");
    }

    @Override
    public Socket getSocket(String name) {
        return activeConnections.get(name);
    }

    @Override
    public Socket addSocket(String name) {
        return activeConnections.get(name);
    }
}
