package DAO;

import Model.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Util.ConnectionUtil;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;


public class AccountDao {


    public List<Account> getAllAccount(){
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try{
            String sql = "SELECT * FROM account";
            PreparedStatement prepStatement = connection.prepareStatement(sql);
            ResultSet rs = prepStatement.executeQuery();

            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                accounts.add(account);
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return accounts;
    }

    


    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        
        try{
            String sql = "INSERT INTO account (username, password) Values (?,?)";
            PreparedStatement prepStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            prepStatement.setString(1, account.getUsername());
            prepStatement.setString(2, account.getPassword());

            prepStatement.executeUpdate();
            ResultSet pResultSet = prepStatement.getGeneratedKeys();
            while(pResultSet.next()){
                int gAuthorid = pResultSet.getInt(1);
                return new Account(gAuthorid, account.getUsername(), account.getPassword());
           
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }


    public Account getloginAccount(Account acc){
        Connection connection = ConnectionUtil.getConnection();
        
        try{
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement prepStatement = connection.prepareStatement(sql);
            
            prepStatement.setString(1, acc.getUsername());
            prepStatement.setString(2, acc.getPassword());

            ResultSet rs = prepStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
               
                return account;
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    public boolean checkUserA(String Username){
        Connection connection = ConnectionUtil.getConnection();
        
        try{
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement prepStatement = connection.prepareStatement(sql);
            
            prepStatement.setString(1, Username);

            ResultSet rs = prepStatement.executeQuery();
            if(rs.next()){          
                return true;
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return false;
    }
    




}
