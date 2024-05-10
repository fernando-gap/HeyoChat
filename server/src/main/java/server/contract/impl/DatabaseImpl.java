package server.contract.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    public void saveMessage(Message message, String senderName, String receiverName) throws SQLException {
        String sql = "insert into UserMessage (senderID, receiverID, message) values (?, ?, ?)";
        PreparedStatement s = connection.prepareStatement(sql);
        s.setString(1, senderName);
        s.setString(2, receiverName);
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
