package gui;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import javax.swing.SwingUtilities;

import shared.communication.Receiver;

public class MessageFromServer implements Runnable {
    private InputStream in;
    private boolean isRunning = true;
    private ScreenRoot root;

    public MessageFromServer(InputStream in) throws IOException {
        this.in = in;
    }

    public void setRoot(ScreenRoot root) {
        this.root = root;
    }

	public void run() {
        System.out.println("[THREAD MessageFromServer] starting...");

        while (true) {
            if (isRunning) {
                try {
                    ObjectInputStream objectIn = new ObjectInputStream(in);
                    Receiver<?> receiver = (Receiver<?>) objectIn.readObject();
                    root.handleUpdate(receiver);
                    System.out.println("message.");
                } catch (IOException e) {
                    System.err.println(e);
                } catch (ClassNotFoundException e) {
                    System.err.println(e);
                }
            }
        }
	}
}
