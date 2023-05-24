package com.example.slowvf.Model;

public class SentMessage {
    private String idReceiver;
    private String texte;
    private String dateWriting;
    private String dateReceived;

    public SentMessage(String idReceiver, String texte, String dateWriting, String dateReceived) {
        this.idReceiver = idReceiver;
        this.texte = texte;
        this.dateWriting = dateWriting;
        this.dateReceived = dateReceived;
    }

    public String getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
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