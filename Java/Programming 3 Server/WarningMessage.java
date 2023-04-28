package com.server;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.Instant;

//Class for creating warning messages 
public class WarningMessage {
    
    private String nick;
    private Double latitude;
    private Double longitude;
    private String dangertype;
    private ZonedDateTime sent;
    private String areacode;
    private String phonenumber;

    //Constructor which includes areacode and phonenumber
    public WarningMessage(String nick, Double latitude, Double longitude, ZonedDateTime sent, String dangertype, String areacode, String phonenumber){

        this.nick = nick;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dangertype = dangertype;
        this.sent = sent;
        this.areacode = areacode;
        this.phonenumber = phonenumber;
    }
    //Constructor for messages that do not include areacode and phonenumber
    public WarningMessage(String nick, Double latitude, Double longitude, ZonedDateTime sent, String dangertype){

        this.nick = nick;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dangertype = dangertype;
        this.sent = sent;
        areacode = null;
        phonenumber = null;
    }
    //Getters and setters and methods which change the format of the timestamp
    public String getNick() {
        return nick;
    }
    public void setNick(String nick) {
        this.nick = nick;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public String getDangertype() {
        return dangertype;
    }
    public void setDangertype(String dangertype) {
        this.dangertype = dangertype;
    }
    
    public ZonedDateTime getSent() {
        return sent;
    }
    public void setSent(long epoch) {
        sent = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneOffset.UTC);
    }

    public long dateAsInt() {
        return sent.toInstant().toEpochMilli();
    }
    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    
}
