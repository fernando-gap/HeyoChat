package shared.communication;

import java.io.Serializable;
import java.sql.Timestamp;

public abstract class Client implements Serializable {
    private Action action;
    private Timestamp timestamp;
    private byte[] file;

    public Client(Action action) {
        this.action = action;
        timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Client(byte[] bytes, Action action) {
        this.action = action;
        file = bytes;
    }

    public byte[] getFileBytes() {
        return file;
    }

    public void setFileBytes(byte[] bytes) {
        file = bytes;
    }

    public Action getAction() {
        return action;
    };

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Response getResponse() {
        return getResponseFromAction();
    }

    public final Response getResponseFromAction() {
        switch (action) {
            case SEND_USER2USER:
            case SEND_USER2GROUP:
            case SEND_FILE2USER: // the message is the file name + size
            case GET_USER_MESSAGES:
            case GET_GROUP_MESSAGES:
                return Response.MESSAGE;
            
            case ADD_USER_CONTACT:
                return Response.USERNAME;

            case CREATE_ACCOUNT:
            case SIGN_IN:
            case CREATE_GROUP:
            case LEAVE_GROUP:
            case DELETE_GROUP:
            case ADD_USER2GROUP:
            case RENAME_GROUP:
                return Response.STATUS;

            case NONE:
                return Response.FORBIDDEN;

            default:
                return Response.FORBIDDEN;
        }
    }
}
