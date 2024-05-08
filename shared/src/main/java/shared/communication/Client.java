package shared.communication;

import java.io.Serializable;
import java.sql.Timestamp;

public abstract class Client implements Serializable {
    private Action action;
    private Timestamp timestamp;

    public Client(Action action) {
        this.action = action;
        timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Action getAction() {
        return action;
    };

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Response getResponse() {
        switch (action) {
            case SEND_USER2USER:
            case SEND_USER2GROUP:
            case GET_NEW_MESSAGES:
            case GET_USER_MESSAGES:
            case GET_GROUP_MESSAGES:
            case GET_ALL_USER_MESSAGES:
            case GET_ALL_GROUP_MESSAGES:
                return Response.MESSAGE;


            case CREATE_ACCOUNT:
            case LOGIN_IN:
                return Response.STATUS;

            case NONE:
                return Response.FORBIDDEN;

            default:
                return Response.FORBIDDEN;
        }
    }
}
