package com.example.forum_project.models;

import java.sql.Timestamp;

/**
 * Modèle représentant un utilisateur du blog
 */
public class Utilisateur {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String role; // MEMBRE ou ADMIN
    private boolean actif;
    private String tokenVerification;
    private Timestamp dateInscription;
    private String photoProfil;
    private String bio;
    
    // Constructeurs
    public Utilisateur() {
    }
    
    public Utilisateur(String nom, String prenom, String email, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = "MEMBRE";
        this.actif = false;
    }
    
    // Getters et Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getMotDePasse() {
        return motDePasse;
    }
    
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public boolean isActif() {
        return actif;
    }
    
    public void setActif(boolean actif) {
        this.actif = actif;
    }
    
    public String getTokenVerification() {
        return tokenVerification;
    }
    
    public void setTokenVerification(String tokenVerification) {
        this.tokenVerification = tokenVerification;
    }
    
    public Timestamp getDateInscription() {
        return dateInscription;
    }
    
    public void setDateInscription(Timestamp dateInscription) {
        this.dateInscription = dateInscription;
    }
    
    public String getPhotoProfil() {
        return photoProfil;
    }
    
    public void setPhotoProfil(String photoProfil) {
        this.photoProfil = photoProfil;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    /**
     * Retourne le nom complet de l'utilisateur
     */
    public String getNomComplet() {
        return prenom + " " + nom;
    }
    
    /**
     * Vérifie si l'utilisateur est un administrateur
     */
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
}
