package com.example.slowvf.Model;

public class ReceivedMessage {
    private String idSender;
    private String texte;
    private String dateWriting;
    private String dateReceived;

    public ReceivedMessage(String idSender, String texte, String dateWriting, String dateReceived) {
        this.idSender = idSender;
        this.texte = texte;
        this.dateWriting = dateWriting;
        this.dateReceived = dateReceived;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public String getDateWriting() {
        return dateWriting;
    }

    public void setDateWriting(String dateWriting) {
        this.dateWriting = dateWriting;
    }

    public String getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(String dateReceived) {
        this.dateReceived = dateReceived;
    }
}