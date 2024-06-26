package shared.communication;

import java.util.ArrayList;

public class Sender<T> extends Client implements Data<T> {
    private String name;
    private String receiverName;
    private ArrayList<T> data = new ArrayList<>();

    public Sender(byte[] bytes, String name, String receiverName, Action action) {
        super(bytes, action);
        this.name = name;
        this.receiverName = receiverName;
    }

    public Sender(Action action, String name) {
        super(action);
        this.name = name;
    }

    public Sender(String name, String receiverName, Action action) {
        super(action);
        this.name = name;
        this.receiverName = receiverName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getName() {
        return name;
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
