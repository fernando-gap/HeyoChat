package server.contract;

import java.sql.SQLException;

import shared.client.Group;
import shared.client.Message;
import shared.client.User;

public interface Database {
    void saveMessage(Message message, String senderName, String receiverName) throws SQLException;
    void saveUserGroup(long userID, long groupID);

    void createGroup(String name);
    void createUser(String name, String password) throws SQLException;


    User getUser(String name) throws SQLException;
    Group getGroup(String name);
    Group[] getAllUserGroup(long userID);


    Message[] getUserMessages(String senderName, String receiverName);
    Message[] getAllUserMessages(String senderName);
    Message[] getGroupMessages(long groupID);
    Message[] getGroupAllMessages(String userName);
}
