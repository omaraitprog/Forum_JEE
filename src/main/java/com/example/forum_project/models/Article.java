package com.example.forum_project.models;

import java.sql.Timestamp;

/**
 * Modèle représentant un article du blog
 */
public class Article {
    private int id;
    private String titre;
    private String contenu;
    private String resume;
    private int auteurId;
    private Utilisateur auteur; // Pour éviter les jointures dans les JSP
    private Timestamp dateCreation;
    private Timestamp dateModification;
    private String statut; // BROUILLON, PUBLIE, ARCHIVE
    private String imageUrl;
    
    // Constructeurs
    public Article() {
    }
    
    public Article(String titre, String contenu, int auteurId) {
        this.titre = titre;
        this.contenu = contenu;
        this.auteurId = auteurId;
        this.statut = "PUBLIE";
    }
    
    // Getters et Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitre() {
        return titre;
    }
    
    public void setTitre(String titre) {
        this.titre = titre;
    }
    
    public String getContenu() {
        return contenu;
    }
    
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
    
    public String getResume() {
        return resume;
    }
    
    public void setResume(String resume) {
        this.resume = resume;
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
    
    public Timestamp getDateModification() {
        return dateModification;
    }
    
    public void setDateModification(Timestamp dateModification) {
        this.dateModification = dateModification;
    }
    
    public String getStatut() {
        return statut;
    }
    
    public void setStatut(String statut) {
        this.statut = statut;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    /**
     * Vérifie si l'article est publié
     */
    public boolean isPublie() {
        return "PUBLIE".equals(statut);
    }
}
