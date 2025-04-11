package DAO;


import Model.Message;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;

import Util.ConnectionUtil;
import java.util.ArrayList;
import java.util.List;


public class MessageDao {

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


    public Message getMessageByMessage(int key){
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
         
            preparedStatement.setInt(1, key);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Message book = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));

                return book;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message createMessage(Message m){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES(?,?,?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);


            preparedStatement.setInt(1, m.getPosted_by());
            preparedStatement.setString(2, m.getMessage_text());
            preparedStatement.setLong(3, m.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet pResultSet = preparedStatement.getGeneratedKeys();

            while(pResultSet.next()){
                int id = pResultSet.getInt(1);
                return new Message(id, m.getPosted_by(), m.getMessage_text(),  m.getTime_posted_epoch());
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }


    public void deleteM(Message message){
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "Delete FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
         
            preparedStatement.setInt(1, message.getMessage_id());

            preparedStatement.executeUpdate();

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    public Message updateM(Message m, int id){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?" ;

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, m.getMessage_text());
            preparedStatement.setInt(2, id);


            int updatedNum = preparedStatement.executeUpdate();

            if(updatedNum > 0){
                sql = "SELECT * FROM message WHERE message_id = ?";
                PreparedStatement prepStatement = connection.prepareStatement(sql);
                prepStatement.setInt(1, id);
                ResultSet rs = prepStatement.executeQuery();

                if(rs.next()){
                    return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                }
            }
            

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;

    }

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
