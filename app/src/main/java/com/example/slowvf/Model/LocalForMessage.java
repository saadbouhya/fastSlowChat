package com.example.slowvf.Model;

import java.io.Serializable;

public class LocalForMessage implements Serializable {

    private final String name;
    private final String id;
    private final String message;
    private final String date_writing;
    private final String date_received;

    public LocalForMessage(String name, String id, String message, String date_writing,String date_received) {
        this.name = name;
        this.id = id;
        this.message = message;
        this.date_writing = date_writing;
        this.date_received = date_received;
    }

    public String getDate_received() {
        return date_received;
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
