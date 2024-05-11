package server;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;
import shared.communication.Receiver;

public class Connection {
	private static int autoIncrementID = 1;
	private int connectionID = 1;
	private SocketChannel channel;
	private ByteBuffer readBuf;
	private ByteBuffer writeBuf;
	private Receiver<?> receiver;
	private int receiverSize;
	private boolean isAuthorized = false;
	private long endat;
	
	public Connection(SocketChannel channel, int size) {
		this.connectionID = Connection.autoIncrementID++;
		this.channel = channel;
		readBuf = ByteBuffer.allocate(size);
	}

	public int getID() {
		return connectionID;
	}

	public void startTimeout(int delay) {
		endat = System.currentTimeMillis() + delay;
	}

	public boolean hasTimeout() {
		return System.currentTimeMillis() >= endat;
	}

	public SocketChannel getChannel() {
		return channel;
	}

	public ByteBuffer getReadBuf() {
		return readBuf;
	}

	public ByteBuffer getWriteBuf(byte[] buf) {
		writeBuf = ByteBuffer.wrap(buf);
		return writeBuf;
	}

	public void setReceiver(Receiver<?> receiver) {
		this.receiver = receiver;
	}

	public void setReceiverSize(int size) {
		receiverSize = size;
	}

	public int getReceiverSize() {
		return receiverSize;
	}

	public Receiver<?> getReceiver() {
		return receiver;
	}
}
