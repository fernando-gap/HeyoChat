package server;

import java.net.Socket;

public class SocketConnection {
    private Socket socket;
    private Boolean isAuthorized = false;

    public SocketConnection(Socket socket) {
        this.socket = socket;
    }

    public Boolean getIsAuthorized() {
        return isAuthorized;
    }

    public void setIsAuthorized(Boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
    }

    public Socket getSocket() {
        return socket;
    }
}
