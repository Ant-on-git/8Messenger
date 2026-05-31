package com.example.a8messenger;

public class Message {
    private String text;
    private String senderId;
    private String recieverId;

    public Message(String text, String senderId, String recieverId) {
        this.text = text;
        this.senderId = senderId;
        this.recieverId = recieverId;
    }

    public Message() {
        //пустой конструктор для бд фаербейса
    }

    public String getText() {
        return text;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getRecieverId() {
        return recieverId;
    }
}
