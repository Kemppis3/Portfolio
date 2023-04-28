package com.server;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;
import com.sun.net.httpserver.Headers;

//Class for handling posted messages to server

public class MessageHandler implements HttpHandler {
    

    private MessageDatabase db = null;

    //Constructor 
    public MessageHandler(MessageDatabase db){
        this.db = db;
        
    }

//Class's handle function for different requests
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        
        if(exchange.getRequestMethod().equalsIgnoreCase("POST")){
           handlePost(exchange);
        }
        else if(exchange.getRequestMethod().equalsIgnoreCase("GET")){
            try {
                handleGet(exchange);
            } catch (SQLException s) {
                System.out.println("SQL Exception caught while handling");
            }
        }
        else{
            sendResponse(exchange, "Not supported", 400);
        }
    }

    //Method handles post-requests, which the server receives. Methods checks the type of the message and acts accordingly to the message content. 
    //Method sends a descriptive message of its action
    private void handlePost(HttpExchange exchange) throws IOException {

        Headers headers = exchange.getRequestHeaders();
        String contentType = "";
        JSONObject jsonObject = null;
        WarningMessage warningMessage = null;
    
        try {
            
            if(headers.containsKey("Content-Type")){
                contentType = headers.get("Content-Type").get(0);
            } else {
                sendResponse(exchange, "No content type", 411);
            }
            
            if(contentType.equalsIgnoreCase("application/json")){
                
                InputStream inputStream = exchange.getRequestBody();
                String msg = new BufferedReader(new InputStreamReader(inputStream,StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
                inputStream.close();
                
                if(msg.length() == 0 || msg == null) {
                    sendResponse(exchange, "Empty message", 412);
                } else {
                    try {
                        jsonObject = new JSONObject(msg);
                    } catch (JSONException j) {
                        System.out.println("JSON parse error");
                    }

                    if(jsonObject.has("query")){
                        if(jsonObject.getString("query").equals("user") || jsonObject.getString("query").equals("time") || jsonObject.getString("query").equals("location")){
                            String userQuery = db.queryMessage(jsonObject);
                            sendResponse(exchange, userQuery, 200);
                        }else {
                            sendResponse(exchange, "Wrong query type", 412);
                        }
                    }

                    if((jsonObject.get("latitude") instanceof Double || jsonObject.get("latitude") instanceof BigDecimal) && (jsonObject.get("longitude") instanceof Double || 
                    jsonObject.get("longitude") instanceof BigDecimal) && (jsonObject.get("sent") instanceof String) && jsonObject.getString("dangertype").equals("Moose") || 
                    jsonObject.getString("dangertype").equals("Reindeer") || jsonObject.getString("dangertype").equals("Deer") || jsonObject.getString("dangertype").equals("Other")){

                    LocalDateTime timeCheck = OffsetDateTime.parse((CharSequence) jsonObject.get("sent")).toLocalDateTime();
                    ZonedDateTime zoneTime = timeCheck.atZone(ZoneId.of("UTC"));
                    if(jsonObject.has("areacode")){
                        warningMessage = new WarningMessage(jsonObject.getString("nickname"), jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude"), zoneTime, jsonObject.getString("dangertype"), jsonObject.getString("areacode"),jsonObject.getString("phonenumber"));
                    } else {
                        warningMessage = new WarningMessage(jsonObject.getString("nickname"), jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude"), zoneTime, jsonObject.getString("dangertype"));
                    }
                    db.setMessage(warningMessage);
                    sendResponse(exchange, "POST successful", 200);
                } else {
                    sendResponse(exchange, "Invalid POST",400);
                }
            }
        } else {
            sendResponse(exchange, "Invalid content type", 407);
        }
    } catch (Exception e) {
            System.out.println(e.getStackTrace());
            sendResponse(exchange, "Internal server error", 500);
        }

    }

    //Method for handling get-requests, which the server receives.
    private void handleGet(HttpExchange exchange) throws IOException, SQLException {

      String responseString = db.getMessage();
      byte [] bytes = responseString.getBytes("UTF-8");
      exchange.sendResponseHeaders(200, bytes.length);
      OutputStream outputStream = exchange.getResponseBody();
      outputStream.write(responseString.getBytes());
      outputStream.flush();
      outputStream.close();
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