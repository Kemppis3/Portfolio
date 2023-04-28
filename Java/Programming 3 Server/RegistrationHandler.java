package com.server;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.util.stream.Collectors;
import com.sun.net.httpserver.Headers;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

//Class for registering new users 

public class RegistrationHandler implements HttpHandler{

    private UserAuthenticator userA = null;
    
    public RegistrationHandler(UserAuthenticator userAuthenticator){

        userA = userAuthenticator;

    }
    
    //Class's function for handling requests

    @Override
    public void handle(HttpExchange exchange) throws IOException {



        try{
            if(exchange.getRequestMethod().equalsIgnoreCase("POST")){
                handlePost(exchange);
        }   else {
                sendResponse(exchange, "Not supported action", 400);
            } 
        } catch (Exception e){
            exchange.sendResponseHeaders(400, -1);
        }
    }

    //Method for handling the post requests. 
    private void handlePost(HttpExchange exchange) throws IOException, SQLException {

        Headers headers = exchange.getRequestHeaders();
        String contentType = "";
        JSONObject jsonObject = null;

            if(headers.containsKey("Content-Type")){
                contentType = headers.get("Content-Type").get(0);
            } else{
                sendResponse(exchange, "No content type", 411);
            }
            if(contentType.equalsIgnoreCase("application/json")){
                InputStream inputStream = exchange.getRequestBody();
                String newUser = new BufferedReader(new InputStreamReader(inputStream,StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
                inputStream.close();
                if(newUser.length() == 0 || newUser == null){
                    sendResponse(exchange, "Invalid user information", 412);
                } else {
                    try {
                        System.out.println("New json created");
                        jsonObject = new JSONObject(newUser);
                    } catch (JSONException j) {
                        System.out.println("JSON parse error");
                    }

                    if(jsonObject.getString("username").length() == 0 || jsonObject.getString("password").length() == 0){
                        sendResponse(exchange, "Invalid user information", 413);
                    } else {
                        System.out.println("Creating new user");
                        Boolean user = userA.addUser(jsonObject.getString("username"), jsonObject.getString("password"), jsonObject.getString("email"));
                        if(user){
                            sendResponse(exchange, "Registration successful", 200);
                        } 
                        sendResponse(exchange, "User already exists", 405);
                    }
                }

            } else {
                sendResponse(exchange, "False content type", 407);
            }

}

    //A method for sending response codes. Simplifies the overall code
    private void sendResponse(HttpExchange exchange, String response, int responseCode) throws IOException {

        
        byte [] bytes = response.getBytes("UTF-8");
        exchange.sendResponseHeaders(responseCode, bytes.length);
        OutputStream outputstream = exchange.getResponseBody();
        outputstream.write(bytes);
        outputstream.flush();
        outputstream.close();

    }
}
