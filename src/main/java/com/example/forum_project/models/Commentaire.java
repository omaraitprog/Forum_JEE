package com.example.forum_project.models;

import java.sql.Timestamp;

/**
 * Modèle représentant un commentaire sur un article
 */
public class Commentaire {
    private int id;
    private String contenu;
    private int articleId;
    private int auteurId;
    private Utilisateur auteur; // Pour éviter les jointures dans les JSP
    private Timestamp dateCreation;
    private boolean approuve;
    
    // Constructeurs
    public Commentaire() {
    }
    
    public Commentaire(String contenu, int articleId, int auteurId) {
        this.contenu = contenu;
        this.articleId = articleId;
        this.auteurId = auteurId;
        this.approuve = true;
    }
    
    // Getters et Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getContenu() {
        return contenu;
    }
    
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
    
    public int getArticleId() {
        return articleId;
    }
    
    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }
    
    public int getAuteurId() {
        return auteurId;
    }
    
    public void setAuteurId(int auteurId) {
        this.auteurId = auteurId;
    }
    
    public Utilisateur getAuteur() {
        return auteur;
    }
    
    public void setAuteur(Utilisateur auteur) {
        this.auteur = auteur;
    }
    
    public Timestamp getDateCreation() {
        return dateCreation;
    }
    
    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }
    
    public boolean isApprouve() {
        return approuve;
    }
    
    public void setApprouve(boolean approuve) {
        this.approuve = approuve;
    }
}
