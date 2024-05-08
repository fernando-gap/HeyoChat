package shared.client;

public class Group implements Customer {
    private String name;
    private long id;
    private static CustomerType type = CustomerType.USER;

    public Group(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public long getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public CustomerType getType() {
        return type;
    }
}
