package server.contract;

public interface Protocol<Sender, Receiver, Socket> {
    Sender receive();
    void send(Receiver client);
    Socket getSocket(String name);
    Socket addSocket(String name);
}
