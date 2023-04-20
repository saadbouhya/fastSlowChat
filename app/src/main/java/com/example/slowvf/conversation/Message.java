package com.example.slowvf.conversation;

public class Message {
    private String contenu;
    private String auteur;
    private String destinataire;

    private String dateTime;

    public Message() {}

    // Constructeur
    public Message(String contenu, String auteur, String destinataire, String dateTime) {
        this.contenu = contenu;
        this.auteur = auteur;
        this.destinataire = destinataire;
        this.dateTime = dateTime;
    }

    // Getters et setters
    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public String getDate() {
        return dateTime;
    }

    public void setDate(String date) {
        this.dateTime = date;
    }
}
