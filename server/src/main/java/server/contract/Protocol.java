package server.contract;

import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ConcurrentLinkedDeque;

public interface Protocol<Connection> extends Runnable {
    public void stop();
    public void setReadyDeque(ConcurrentLinkedDeque<Connection> readyDeque);
    public void setServer(ServerSocketChannel server);
    public Connection getConnection(String name);
    public void cacheConnection(String name, Connection connection);
}
