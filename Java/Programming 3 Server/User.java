package com.server;


// Simple class for creating user-objects with username, password and an email address. 

public class User {
    
private String username;
private String password;
private String emailAddress;


public User(){

}
//Default constructor
public User(String username, String password, String emailAddress){
    this.username = username;
    this.password = password;
    this.emailAddress = emailAddress;
}

//Getters
public String getUserName(){
    return username;
}
public String getPassword(){
    return password;
}
public String getEmail(){
    return emailAddress;
}

}
