package com.example.slowvf.Model;

public class SentMessage {
    private String id_receiver;
    private String texte;
    private String date_writing;
    private String date_received;

    public SentMessage(String id_receiver, String texte, String date_writing, String date_received) {
        this.id_receiver = id_receiver;
        this.texte = texte;
        this.date_writing = date_writing;
        this.date_received = date_received;
    }

    public String getId_receiver() {
        return id_receiver;
    }

    public void setId_receiver(String id_receiver) {
        this.id_receiver = id_receiver;
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