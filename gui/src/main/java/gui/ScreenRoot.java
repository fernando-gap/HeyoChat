package gui;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ScreenRoot extends javax.swing.JFrame {

    private static Socket socket = null;

    public ScreenRoot() {
        socket = getSocket();
        add(new ScreenAuthentication()); /* first screen */
    }

    public static void setSocket(Socket socket) {
        ScreenRoot.socket = socket;
    }

    public static Socket getSocket() {
        if (socket == null) {
            try {
                socket = new Socket("localhost", 5000);
                socket.setKeepAlive(true);
                socket.setReuseAddress(true);
            } catch (SocketException e) {
                System.err.println(e);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        return socket;
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ScreenRoot root = new ScreenRoot();
                root.pack();
                root.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    // End of variables declaration                   
}
