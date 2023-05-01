package com.example.slowvf.Model;

public class LocalForConversation {
    private String contenu;
    private String auteur;
    private String destinataire;

    private String date_received;
    private String date_writing;

    public LocalForConversation() {}

    // Constructeur
    public LocalForConversation(String contenu, String auteur, String destinataire, String date_received, String date_writing) {
        this.contenu = contenu;
        this.auteur = auteur;
        this.destinataire = destinataire;
        this.date_received = date_received;
        this.date_writing = date_writing;
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

    public String getDate_received() {
        return date_received;
    }

    public void setDate_received(String date_received) {
        this.date_received = date_received;
    }

    public String getDate_writing() {
        return date_writing;
    }

    public void setDate_writing(String date_writing) {
        this.date_writing = date_writing;
    }
}
