package com.example.test;

public class msgModelClass {
    String message;
    String senderId;
    long timeStmp;

    public msgModelClass() {

    }

    public msgModelClass(String message, String senderId, long timeStmp) {
        this.message = message;
        this.senderId = senderId;
        this.timeStmp = timeStmp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimeStmp() {
        return timeStmp;
    }

    public void setTimeStmp(long timeStmp) {
        this.timeStmp = timeStmp;
    }
}
