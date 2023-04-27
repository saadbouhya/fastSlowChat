package com.example.slowvf.model;
import java.util.ArrayList;
import java.util.List;

public class Local {
    private String id_local;
    private List<SentMessage> sent_messages;
    private List<ReceivedMessage> received_messages;
    private String size_file;

    public Local(String id_local,String size_file) {
        this.id_local = id_local;
        this.sent_messages = new ArrayList<>();
        this.received_messages = new ArrayList<>();
        this.size_file = size_file;
    }

    public String getSize_file() {
        return size_file;
    }

    public void setSize_file(String size_file) {
        this.size_file = size_file;
    }

    public String getId_local() {
        return id_local;
    }

    public void setId_local(String id_local) {
        this.id_local = id_local;
    }

    public List<SentMessage> getSent_messages() {
        return sent_messages;
    }

    public void setSent_messages(List<SentMessage> sent_messages) {
        this.sent_messages = sent_messages;
    }

    public List<ReceivedMessage> getReceived_messages() {
        return received_messages;
    }

    public void setReceived_messages(List<ReceivedMessage> received_messages) {
        this.received_messages = received_messages;
    }
}