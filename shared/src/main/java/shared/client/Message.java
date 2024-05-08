package shared.client;

public class Message {
    private String message;
    private String creationDate;
    private String senderName;
    private String receiverName;

    public Message(String message, String receiverName, String senderName, String creationDate) {
        this.message = message;
        this.receiverName = receiverName;
        this.senderName = senderName;
        this.creationDate = creationDate;
    }

    public String getMessage() {
        return message;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getSenderName() {
        return senderName;
    }
}
