package com.example.slowvf.model;

public class MessageEchange {
    private String id_sender;
    private String id_receiver;
    private String date_writing;
    private String message_text;
    private String date_received;

    public String getId_sender() {
        return id_sender;
    }

    public void setId_sender(String id_sender) {
        this.id_sender = id_sender;
    }

    public String getId_receiver() {
        return id_receiver;
    }

    public void setId_receiver(String id_receiver) {
        this.id_receiver = id_receiver;
    }

    public String getDate_writing() {
        return date_writing;
    }

    public void setDate_writing(String date_writing) {
        this.date_writing = date_writing;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public String getDate_received() {
        return date_received;
    }

    public void setDate_received(String date_received) {
        this.date_received = date_received;
    }
}