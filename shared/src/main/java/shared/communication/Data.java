package shared.communication;

public interface Data<T> {
    public T getData(int index);
    public void appendData(T data);
}
