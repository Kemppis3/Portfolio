package com.server;

import com.sun.net.httpserver.BasicAuthenticator;

import java.sql.SQLException;

import org.json.JSONObject;

//Class for autchentication check
public class UserAuthenticator extends BasicAuthenticator {

    private MessageDatabase database = null;


    public UserAuthenticator(MessageDatabase db) {
        super("warning");
        database = db;
        
    }
    //Checks user's credentials by doing a query to the database with given parameters
    public boolean checkCredentials(String username, String password) {
    
        System.out.println("Checking user: " + username + " " + password);
        try{
            return database.authenticateUser(username, password);
        } catch (SQLException s) {
            System.out.println("SQL Exception in checkCredentials");
            return false;
        }
    }
    //Adds user to the database with given parameters
    public boolean addUser(String username, String password, String email) throws SQLException{
        JSONObject obj = new JSONObject();
        obj.put("username", username).put("password", password).put("email", email);
        return database.setUser(obj);
    }
    
}
