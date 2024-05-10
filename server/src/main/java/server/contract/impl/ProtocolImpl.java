package server.contract.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;

import server.Connection;
import server.contract.Protocol;



public class ProtocolImpl implements Protocol<Connection> {
	private ConcurrentLinkedDeque<Connection> readyDeque;
	private HashMap<String, Connection> connections;
	private ServerSocketChannel server;
	private boolean isRunning = false;
	private Selector selector;

    public ProtocolImpl() {
		try {
			selector = Selector.open();
		} catch (IOException e) {
			System.out.println(e);
			System.exit(1);
		}
    }

    @Override
    public void setReadyDeque(ConcurrentLinkedDeque<Connection> readyDeque) {
        this.readyDeque = readyDeque;
    }

    @Override
    public void setServer(ServerSocketChannel server) {
        this.server = server;
		try {
            server.configureBlocking(false);
		} catch (IOException e) {
			System.out.println(e);
			System.exit(1);
		}
    }

	public Connection getConnection(String name) {
		return connections.get(name);
	}

	public void cacheConnection(String name, Connection connection) {
		connections.put(name, connection);
	}

    @Override
    public void stop() {
        isRunning = false;
    }

    @Override
    public void run() {
		if (server != null && readyDeque != null) {
			isRunning = true;
		} else {
			System.err.println("[ERROR]: ReadyDeque or Server not set");
			System.exit(1);
		}

		System.out.println("[THREAD ReceiveData]: Waiting data from clients...");
		
		while(isRunning) {
			try {
				SocketChannel client = server.accept();

				if (client != null) {
					client.configureBlocking(false);
					SelectionKey k = client.register(selector, SelectionKey.OP_READ);
					Connection c = new Connection(client, 10*10000);
					k.attach(c);
					System.out.println("[STATUS]: client received.");
				}

				selector.selectNow(new Consumer<SelectionKey>() {
					public void accept(SelectionKey k) {
						if (k.isReadable()) {
							try {
								Connection client = (Connection) k.attachment();
								ByteBuffer buf = client.getReadBuf();
								SocketChannel channel = client.getChannel();

								if (buf.position() == 0) {
									System.out.println("[STATUS]: timeout started");
									client.startTimeout(200);
								}

								/* only read data within timeout */
								if (!client.hasTimeout()) {
									int status = channel.read(buf);
									System.out.println("[STATUS]: add to waitingQueue");

									if (!readyDeque.contains(client)) {
										readyDeque.add(client);
									}
									if (status == -1) { System.err.println("END OF STREAM"); k.cancel(); }
								}
								System.out.println("[STATUS]: client has data to read.");
							} catch (IOException e) {
								System.out.println(e);
							}
						} 
					}
				});
			} catch (IOException e) {
				System.err.println(e);
			} 
		}
    }
}
