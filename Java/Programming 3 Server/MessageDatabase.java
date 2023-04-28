package com.server;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.apache.commons.codec.digest.Crypt;
import org.json.JSONArray;
import org.json.JSONObject;

/*Class for creating the database for users and messages.
Class also includes methods for getting messages ana user information from the database, setting messages and users to the database,
doing queries to the database
*/
public class MessageDatabase {
    
    private Connection dbConnection = null;
    private JSONArray responseArray = new JSONArray();
    
    
    public MessageDatabase(){

        try{
            openDataBase();
        } catch (SQLException s) {
            System.out.println("Creating database failed or the database already exists!");
        }
    }
    //Initializes the database
    private boolean init() throws SQLException {
        String databaseName = "MessageDatabase";
        String database = "jdbc:sqlite:" + databaseName;
        dbConnection = DriverManager.getConnection(database);
    
        if(dbConnection != null) {
            String userTable = "CREATE TABLE users (username varchar(50) UNIQUE NOT NULL, password varchar(50) NOT NULL, email varchar(50) UNIQUE, PRIMARY KEY(username))";
            Statement userStatement = dbConnection.createStatement();
            userStatement.executeUpdate(userTable);
            userStatement.close();

            String messageTable = "CREATE TABLE messages (nickname varchar(50) NOT NULL, latitude REAL(50) NOT NULL, longitude REAL(50) NOT NULL, sent varchar(50) NOT NULL, dangertype varchar(50) NOT NULL, areacode varchar(50), phonenumber varchar(50))";
            Statement messageStatement = dbConnection.createStatement();
            messageStatement.executeUpdate(messageTable);
            messageStatement.close();

            System.out.println("Database created successfully");
            return true;
        }

        System.out.println("Error while creating database");
        return false;
    }
    //Opens the database
    public void openDataBase() throws SQLException {
        String databaseName = "MessageDatabase";
        String database = "jdbc:sqlite:" + databaseName;
        File databasePath = new File(database);
        boolean validPath = databasePath.exists();
        if(validPath) {
            dbConnection = DriverManager.getConnection(database);
        } else {
            init();
        }
    }
    //Closes the database
    public void closeDataBase() throws SQLException {
        if(dbConnection != null) {
            dbConnection.close();
            System.out.println("Closing connecting to the database");
            dbConnection = null;
        }
    }

    public void setMessage(WarningMessage warningMessage) throws SQLException {
        String messageFormat = "INSERT INTO messages " + "VALUES('" + warningMessage.getNick() + "','" + warningMessage.getLatitude() + "','" + warningMessage.getLongitude() + "','" + warningMessage.dateAsInt() + "','" + warningMessage.getDangertype() + "','" + warningMessage.getAreacode() + "','" + warningMessage.getPhonenumber() + "')";
        System.out.println(messageFormat);
        Statement messageStatement = dbConnection.createStatement();
        messageStatement.executeUpdate(messageFormat);
        messageStatement.close();
    }

    public String getMessage() throws SQLException {
        String getMessageString = "SELECT nickname, latitude, longitude, sent, dangertype, areacode, phonenumber FROM messages";
        Statement queryMessageStatement = dbConnection.createStatement();
        ResultSet resultSet = queryMessageStatement.executeQuery(getMessageString);

        while(resultSet.next()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nickname", resultSet.getString(1));
            jsonObject.put("latitude", resultSet.getDouble(2));
            jsonObject.put("longitude", resultSet.getDouble(3));
            ZonedDateTime zonedTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(resultSet.getLong(4)), ZoneOffset.UTC);
            jsonObject.put("sent",zonedTime);
            jsonObject.put("dangertype", resultSet.getString(5));
            if(!resultSet.getString("areacode").equals("null")){
                jsonObject.put("areacode", resultSet.getString(6));
                jsonObject.put("phonenumber", resultSet.getString(7));
            }
            responseArray.put(jsonObject);
        }

        return responseArray.toString();
    }

    //Checks if the user exists in the database
    public boolean checkUser(String username) throws SQLException {

        Statement query = null;
        ResultSet resultSetUser;
    
        String user = "SELECT username FROM users WHERE username = '" + username + "'";
    
        query = dbConnection.createStatement();
        resultSetUser = query.executeQuery(user);

        if(resultSetUser.next()){
            System.out.println("User exists");
            return true;
        } else {
            return false;
        }
        
        
    }
    //Inserts the user into the database with given information
    public boolean setUser(JSONObject user) throws SQLException{
        if (checkUser(user.getString("username"))){
            return false;
        }
        String setUser = "INSERT INTO users " + "VALUES('" + user.getString("username") +"','" + Crypt.crypt(user.getString("password")) + "','" + user.getString("email") + "')";
        Statement newStatement;
        newStatement = dbConnection.createStatement();
        newStatement.executeUpdate(setUser);
        newStatement.close();
        return true;
    }

    //Method for autchenticating the user does not exist and hasging the user's password
    public boolean authenticateUser(String username, String userPassword) throws SQLException {

        String userCheck = "SELECT username, password FROM users WHERE username = '" + username + "'";

        Statement query = dbConnection.createStatement();
        ResultSet resultSet = query.executeQuery(userCheck);

        if(!resultSet.next()){
            System.out.println("User not found");
            return false;
        } else {
            String hashedPassword = resultSet.getString("password");
            return hashedPassword.equals(Crypt.crypt(userPassword, hashedPassword));
        }
    }
    
    //Method created for features 7 and 9. User can ask for danger messages from specific user, specific time windod or specific location
    public String queryMessage(JSONObject object) throws SQLException {

        String queryMessage;

        if(object.getString("query").equals("time")){
            
            responseArray.clear();
            String timeStart = object.getString("timestart");
            String timeEnd = object.getString("timeend");
            LocalDateTime timeStartChange = OffsetDateTime.parse((CharSequence) timeStart).toLocalDateTime();
            ZonedDateTime startZoneTime = timeStartChange.atZone(ZoneId.of("UTC"));
            LocalDateTime timeEndtChange = OffsetDateTime.parse((CharSequence) timeEnd).toLocalDateTime();
            ZonedDateTime endZoneTime = timeEndtChange.atZone(ZoneId.of("UTC"));
            
            queryMessage = "SELECT * FROM messages WHERE sent BETWEEN '" + startZoneTime + "' AND '" + endZoneTime + "'";
        }
        
        else if(object.getString("query").equals("location")){
        
        responseArray.clear();
        Double uplongitude = object.getDouble("uplongitude");
        Double uplatitude = object.getDouble("uplatitude");
        Double downlongitude = object.getDouble("downlongitude");
        Double downlatitude = object.getDouble("downlatitude");

        queryMessage = "SELECT * FROM messages WHERE latitude BETWEEN '" + downlatitude + "' AND '" + uplatitude + "' AND longitude BETWEEN '" + uplongitude + "' AND '" + downlongitude + "'";

    }
    
        else {

        responseArray.clear();
        queryMessage= "SELECT * FROM messages WHERE nickname LIKE '%" + object.getString("nickname") + "%'";
    }

    Statement timeQuery = dbConnection.createStatement();
    ResultSet timeResultSet = timeQuery.executeQuery(queryMessage);
    while(timeResultSet.next()){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nickname", timeResultSet.getString(1));
        jsonObject.put("latitude", timeResultSet.getDouble(2));
        jsonObject.put("longitude", timeResultSet.getDouble(3));
        ZonedDateTime zonedTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timeResultSet.getLong(4)), ZoneOffset.UTC);
        jsonObject.put("sent",zonedTime);
        jsonObject.put("dangertype", timeResultSet.getString(5));
        if(!timeResultSet.getString("areacode").equals("null")){
            jsonObject.put("areacode", timeResultSet.getString(6));
            jsonObject.put("phonenumber", timeResultSet.getString(7));
        }
        responseArray.put(jsonObject);
        
    }   

    return responseArray.toString();

}
}
