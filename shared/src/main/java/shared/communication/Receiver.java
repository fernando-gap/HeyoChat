package shared.communication;

import java.util.ArrayList;

public class Receiver<T> extends Client implements Data<T> {
    private String senderName;
    private Response customResponse;
    private String errorMessage;
    private ArrayList<T> data = new ArrayList<>();

    public Receiver(Action actionPerformed, Response response) {
        super(actionPerformed);
        customResponse = response;
    }

    public Receiver(Action actionPerformed) {
        super(actionPerformed);
    }

    public Receiver(String senderName, Action actionPerformed) {
        super(actionPerformed);
        this.senderName = senderName;
    }

    public Receiver(String senderName, Action actionPerformed, Response response) {
        super(actionPerformed);
        this.senderName = senderName;
    }

    public void resetResponse() {
        customResponse = null;
    }

    public void setCustomResponse(Response customResponse) {
        this.customResponse = customResponse;
    }

    public void setErrorMessage(String errorMessage) {
        this.customResponse = Response.ERROR;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public Response getResponse() {
        if (customResponse != null) return customResponse;
        return super.getResponse();
    }

    public String getSenderName() {
        return senderName;
    }

    public ArrayList<T> getArrayList() {
        return data;
    }

    public boolean hasError() {
        if (customResponse == Response.ERROR) {
            return true;
        }
        return false;
    }

    @Override
    public T getData(int index) {
        return data.get(index);
    }

    @Override
    public void appendData(T data) {
        this.data.add(data);
    }
}