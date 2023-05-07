package com.example.slowvf.Model;

public class InAppMessage {
    private String contenu;
    private String auteur;
    private String destinataire;

    private String dateTime;

    public InAppMessage() {}

    // Constructeur
    public InAppMessage(String contenu, String auteur, String destinataire, String dateTime) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InAppMessage that = (InAppMessage) o;

        if (auteur != null ? !auteur.equals(that.auteur) : that.auteur != null) return false;
        if (destinataire != null ? !destinataire.equals(that.destinataire) : that.destinataire != null)
            return false;
        return dateTime != null ? dateTime.equals(that.dateTime) : that.dateTime == null;
    }

}
