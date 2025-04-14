package DAO;


import Model.Message;

import java.sql.*;

import Util.ConnectionUtil;
import java.util.ArrayList;
import java.util.List;

/*
 * The MessageDao handles database operations for the message table.
  * It provides methods to interact with messages, such as retrieving, inserting, updating, and deleting message data
  */
public class MessageDao {
    /*
     * retrieves all message from the database
     * Just here to help debug
     * @return List<Message> A list of message objects representing all rows in the message table
     */
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message";
            PreparedStatement prepStatement = connection.prepareStatement(sql);
            ResultSet rs = prepStatement.executeQuery();

            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return messages;
    }

    /*
     * getMessageByMessage retrieves a message by its message_id
     * @param key the message_id to search for.
     * @return Message the message object matching the provided messageid or null if not found
     */
    public Message getMessageByMessage(int key){
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement prepStatement = connection.prepareStatement(sql);
         
            prepStatement.setInt(1, key);

            ResultSet rs = prepStatement.executeQuery();

            while(rs.next()){
                Message book = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));

                return book;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * createMessage inserts a new message into the database
     * 
     * @param m the message object containing the message data to be inserted
     * @return Message the inserted nessage or null if insert failed
     */
    public Message createMessage(Message m){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES(?,?,?)" ;
            PreparedStatement prepStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);


            prepStatement.setInt(1, m.getPosted_by());
            prepStatement.setString(2, m.getMessage_text());
            prepStatement.setLong(3, m.getTime_posted_epoch());

            prepStatement.executeUpdate();
            ResultSet pResultSet = prepStatement.getGeneratedKeys();

            while(pResultSet.next()){
                int id = pResultSet.getInt(1);
                return new Message(id, m.getPosted_by(), m.getMessage_text(),  m.getTime_posted_epoch());
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * deleteM deletes a message from the database by message_id
     * @param message the message object to be deleted from the database
     */
    public void deleteM(Message message){
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "Delete FROM message WHERE message_id = ?";
            PreparedStatement prepStatement = connection.prepareStatement(sql);
         
            prepStatement.setInt(1, message.getMessage_id());

            prepStatement.executeUpdate();

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    

    /*
     * updateM updates the messagetext of an existing message in the databas
     * @param m the message object
     * @param id the messageid of the message to be updated
     * @return Message the updated message object or null if the update fails
     */
    public Message updateM(Message m, int id){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?" ;

            PreparedStatement prepStatement = connection.prepareStatement(sql);
            prepStatement.setString(1, m.getMessage_text());
            prepStatement.setInt(2, id);


            int updatedNum = prepStatement.executeUpdate();

            if(updatedNum > 0){
                sql = "SELECT * FROM message WHERE message_id = ?";
                PreparedStatement prepStatement1 = connection.prepareStatement(sql);
                prepStatement1.setInt(1, id);
                ResultSet rs = prepStatement1.executeQuery();

                if(rs.next()){
                    return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                }
            }
            

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;

    }

    /*
     * getAllMbyUser retrieves all messages posted by a specific user from the database
     * @param postedby The user_id of the user whose messages are to be retrieved from
     * @return List<Message> A list of message objects posted by the user 
     */
    public List<Message> getAllMbyUser(int postedby){
        Connection connection = ConnectionUtil.getConnection();
        
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement prepStatement = connection.prepareStatement(sql);
            prepStatement.setInt(1, postedby);
            ResultSet rs = prepStatement.executeQuery();
            
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return messages;
    }


}
