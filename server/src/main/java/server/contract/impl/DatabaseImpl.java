package server.contract.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import server.contract.Database;
import shared.client.Group;
import shared.client.Message;
import shared.client.User;

public class DatabaseImpl implements Database {

    private Connection connection;

    public DatabaseImpl() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:4000/heyochat", "root", "app1234");
        createTables();
    }

    @Override
    public void getAllContacts(String name, ArrayList<String> result) throws SQLException {
        String sql = "SELECT userName FROM User u " +
                "INNER JOIN " +
                "(SELECT receiverID id FROM UserMessage ums " +
                "WHERE ums.senderID = ? " +
                "UNION " +
                "SELECT senderID FROM UserMessage umr " +
                "WHERE umr.receiverID = ?) c " +
                "ON u.userID = c.id";

        PreparedStatement s = connection.prepareStatement(sql);
        User user = getUser(name);

        if (user == null) {
            throw new Error("Forbidden Operation.");
        }

        s.setInt(1, (int) user.getID());
        s.setInt(2, (int) user.getID());
        ResultSet query = s.executeQuery();

        while (query.next()) {
            if (query.getString("userName") == null) {
                return;
            }
            result.add(query.getString("userName"));
        }
    }

    @Override
    public void saveMessage(Message message, String senderName, String receiverName) throws SQLException {
        String sql = "insert into UserMessage (senderID, receiverID, message) values (?, ?, ?)";
        PreparedStatement s = connection.prepareStatement(sql);

        User sender = getUser(senderName);
        User receiver = getUser(receiverName);

        if (sender == null || receiver == null) {
            throw new Error("Usernames are not valid.");
        }

        s.setLong(1, sender.getID());
        s.setLong(2, receiver.getID());
        s.setString(3, message.getMessage());
        s.executeUpdate();
    }

    @Override
    public void saveUserGroup(long userID, long groupID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveUserGroup'");
    }

    @Override
    public void createGroup(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createGroup'");
    }

    @Override
    public void getNewMessages(String userName, ArrayList<Message> m) throws SQLException {
        String sql = "SELECT um1.userName AS sender, um2.userName AS receiver, um.message, um.createdAt "
                + "FROM UserMessage um "
                + "INNER JOIN User um1 ON um.senderID = um1.userID "
                + "INNER JOIN User um2 ON um.receiverID = um2.userID "
                + "WHERE um.senderID = ? OR um.receiverID = ? "
                + "ORDER BY um.createdAt ASC";

        try {
            PreparedStatement s = connection.prepareStatement(sql);
            User sender = getUser(userName);
            s.setInt(1, (int) sender.getID());
            s.setInt(2, (int) sender.getID());
            System.err.println(s);
            ResultSet query = s.executeQuery();

            while (query.next()) {
                String sName = query.getString("sender");
                String rName = query.getString("receiver");
                Message msg = new Message(query.getString("message"), sName, rName, query.getTimestamp("createdAt"));
                m.add(msg);
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    @Override
    public void createUser(String name, String password) throws SQLException {
        String sql = "insert into User(userName, userPassword) values (?, ?)";
        PreparedStatement s = connection.prepareStatement(sql);
        s.setString(1, name);
        s.setString(2, password);
        s.executeUpdate();
    }

    @Override
    public User getUser(String name) throws SQLException {
        String sql = "select * from User where userName = ?";
        PreparedStatement s = connection.prepareStatement(sql);
        s.setString(1, name);
        ResultSet result = s.executeQuery();

        while (result.next()) {
            if (result.getString("userName") == null) {
                return null;
            }

            return new User(result.getInt("userID"), name, result.getString("userPassword"));
        }
        return null;
    }

    @Override
    public Group getGroup(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGroup'");
    }

    @Override
    public Group[] getAllUserGroup(long userID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllUserGroup'");
    }

    @Override
    public Message[] getUserMessages(String senderName, String receiverName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserMessages'");
    }

    @Override
    public Message[] getAllUserMessages(String senderName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllUserMessages'");
    }

    @Override
    public Message[] getGroupMessages(long groupID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGroupMessages'");
    }

    @Override
    public Message[] getGroupAllMessages(String userName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGroupAllMessages'");
    }

    private void createTables() {
        try (Statement s = connection.createStatement()) {
            String tableUser = "CREATE TABLE IF NOT EXISTS User (" +
                    "userID INT PRIMARY KEY AUTO_INCREMENT," +
                    "userName VARCHAR(200) UNIQUE," +
                    "userPassword VARCHAR(50) NOT NULL," +
                    "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";

            String tableUserMessage = "CREATE TABLE IF NOT EXISTS UserMessage("
                    + "messageID INT PRIMARY KEY AUTO_INCREMENT, "
                    + "senderID INT NOT NULL, "
                    + "receiverID INT NOT NULL, "
                    + "message VARCHAR(500) NOT NULL, "
                    + "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "FOREIGN KEY (senderID) REFERENCES User(userID), "
                    + "FOREIGN KEY (receiverID) REFERENCES User(userID)"
                    + ");";

            s.executeUpdate(tableUser);
            s.executeUpdate(tableUserMessage);
            System.out.println("Creation of Tables Executed");

        } catch (SQLException e) {
            System.err.println(e);
            System.exit(1);
        }
    }
}
