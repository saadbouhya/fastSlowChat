package com.example.slowvf.Model;

import java.io.Serializable;

public class LocalForMessage implements Serializable {

    private final String name;
    private final String id;
    private final String message;
    private final String dateWriting;
    private final String dateReceived;

    public LocalForMessage(String name, String id, String message, String dateWriting, String dateReceived) {
        this.name = name;
        this.id = id;
        this.message = message;
        this.dateWriting = dateWriting;
        this.dateReceived = dateReceived;
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

    public String getDateWriting() {
        return dateWriting;
    }
}
