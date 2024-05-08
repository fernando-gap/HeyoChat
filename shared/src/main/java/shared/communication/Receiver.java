package shared.communication;

import java.util.ArrayList;

public class Receiver<T> extends Client implements Data<T> {
    private String senderName;
    private Response response;
    private ArrayList<T> data = new ArrayList<>();

    public Receiver(Action actionPerformed, Response response) {
        super(actionPerformed);
    }

    public Receiver(Action actionPerformed) {
        super(actionPerformed);
    }

    public Receiver(String senderName, Action actionPerformed) {
        super(actionPerformed);
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    public boolean hasError() {
        if (response == Response.ERROR) {
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
