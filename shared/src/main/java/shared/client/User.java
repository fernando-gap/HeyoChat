package shared.client;

import java.io.Serializable;

public class User implements Customer, Serializable {
    private long id;
    private String name;
    private String password;
    private static CustomerType type = CustomerType.GROUP;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public void setPasswordNull() {
        this.password = null;
    }

    @Override
    public long getID() {
        return id;
    }

    @Override
    public CustomerType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
