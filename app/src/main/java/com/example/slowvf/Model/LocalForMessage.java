package com.example.slowvf.Model;

public class LocalForMessage {

    private final String name;
    private final String id;
    private final String message;
    private final String date_writing;

    public LocalForMessage(String name, String id, String message, String date_writing) {
        this.name = name;
        this.id = id;
        this.message = message;
        this.date_writing = date_writing;
    }

    public String getName2() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getDate_writing() {
        return date_writing;
    }
}
