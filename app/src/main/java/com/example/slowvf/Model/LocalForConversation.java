package com.example.slowvf.Model;

public class LocalForConversation {
    private String contenu;
    private String auteur;
    private String destinataire;

    private String dateReceived;
    private String dateWriting;

    public LocalForConversation() {}

    // Constructeur
    public LocalForConversation(String contenu, String auteur, String destinataire, String dateReceived, String dateWriting) {
        this.contenu = contenu;
        this.auteur = auteur;
        this.destinataire = destinataire;
        this.dateReceived = dateReceived;
        this.dateWriting = dateWriting;
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

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public String getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(String dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getDateWriting() {
        return dateWriting;
    }

    public void setDateWriting(String dateWriting) {
        this.dateWriting = dateWriting;
    }
}
