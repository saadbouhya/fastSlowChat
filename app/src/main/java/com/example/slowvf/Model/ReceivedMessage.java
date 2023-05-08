package com.example.slowvf.Model;

public class ReceivedMessage {
    private String id_sender;
    private String texte;
    private String date_writing;
    private String date_received;

    public ReceivedMessage(String id_sender, String texte, String date_writing, String date_received) {
        this.id_sender = id_sender;
        this.texte = texte;
        this.date_writing = date_writing;
        this.date_received = date_received;
    }

    public String getId_sender() {
        return id_sender;
    }

    public void setId_sender(String id_sender) {
        this.id_sender = id_sender;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public String getDate_writing() {
        return date_writing;
    }

    public void setDate_writing(String date_writing) {
        this.date_writing = date_writing;
    }

    public String getDate_received() {
        return date_received;
    }

    public void setDate_received(String date_received) {
        this.date_received = date_received;
    }
}