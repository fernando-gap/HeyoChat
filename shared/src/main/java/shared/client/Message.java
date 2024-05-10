package shared.client;

import java.io.Serializable;

public class Message implements Serializable {
    private String message;
    private String senderName;
    private String receiverName;
    private long createdAt;

    public Message(String message) {
        this.message = message;
    }

    public Message(String message, long timestamp) {
        this.message = message;
        createdAt = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getSenderName() {
        return senderName;
    }
}
