package server.contract.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
    public void saveMessage(Message[] message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveMessage'");
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
    public User getUser(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUser'");
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
            
            s.executeUpdate(tableUser);
            System.out.println("Creation of Tables Executed");

        } catch (SQLException e) {
            System.err.println(e);
        }


    }
}
