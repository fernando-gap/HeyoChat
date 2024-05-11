package shared.client;

import java.io.Serializable;
import java.sql.Timestamp;

public class Message implements Serializable {
    private String message;
    private String senderName;
    private String receiverName;
    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    public Message(String message) {
        this.message = message;
    }

    public Message(String message, String sender, String receiver) {
        this.message = message;
        senderName = sender;
        receiverName = receiver;
    }

    public Message(String message, String sender, String receiver, Timestamp timestamp) {
        this(message, sender, receiver);
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getSenderName() {
        return senderName;
    }
}
