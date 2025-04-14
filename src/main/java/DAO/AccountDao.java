package DAO;

import Model.Account;

import Util.ConnectionUtil;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;

/*
 * The AccountDao handles database operations for the account table
 * It provides methods to interact with user accounts, such as retrieving, inserting, and checking account data
 */
public class AccountDao {


    /*
     * Retrieves all accounts from the database
     * Just here to help debug
     * @return List<Account> list of account from account table
     */
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

    /* Checks if an account with a specific ID exists in the database
     * @param key The account_id to check for existence in the database
     * @return boolean
     */
    public boolean checkPost(int key){
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement prepStatement = connection.prepareStatement(sql);
         
            prepStatement.setInt(1, key);

            ResultSet rs = prepStatement.executeQuery();

            return rs.next();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return false;
    }


    /*
     * Inserts a new account into the database
     * @param account the Account object containing the username and password to be inserted. No need to include account_id since its an autoincremented 
     * @return Account the newly inserted Account with the generated account_id, null if unsuccessful
     */
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

    /*
     * Retrieves an account from the database based on the provided username and password
     * This is used for login authentication
     * 
     * @param acc Account object containing the username and password to verify
     * @return Account the account object if the username and password match, null if no match found
     */
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

    /*
     * Checks if a username already exists in the database
     * @param Username Username string thats being used to check with
     * @return boolean true if the username exists and false otherwise
     */
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
