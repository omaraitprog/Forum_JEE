package com.example.forum_project.services;

import com.example.forum_project.dao.UtilisateurDAO;
import com.example.forum_project.models.Utilisateur;
import com.example.forum_project.utils.EmailService;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

/**
 * Service pour la gestion des utilisateurs
 * Contient la logique métier liée aux utilisateurs
 */
public class UtilisateurService {
    private UtilisateurDAO utilisateurDAO;
    private EmailService emailService;
    
    public UtilisateurService() {
        this.utilisateurDAO = new UtilisateurDAO();
        this.emailService = new EmailService();
    }
    
    /**
     * Authentifie un utilisateur avec son email et mot de passe
     * @param email Email de l'utilisateur
     * @param motDePasse Mot de passe en clair
     * @return Utilisateur si authentification réussie, null sinon
     */
    public Utilisateur authentifier(String email, String motDePasse) {
        Utilisateur utilisateur = utilisateurDAO.findByEmail(email);
        
        if (utilisateur != null && utilisateur.isActif()) {
            // Vérifier le mot de passe avec BCrypt
            if (BCrypt.checkpw(motDePasse, utilisateur.getMotDePasse())) {
                return utilisateur;
            }
        }
        
        return null;
    }
    
    /**
     * Inscrit un nouvel utilisateur
     * @param utilisateur Utilisateur à inscrire
     * @return Le token de vérification si l'inscription a réussi, null sinon
     */
    public String inscrire(Utilisateur utilisateur) {
        // Vérifier que l'email n'existe pas déjà
        if (utilisateurDAO.emailExists(utilisateur.getEmail())) {
            return null;
        }
        
        // Hasher le mot de passe avec BCrypt
        String hashedPassword = BCrypt.hashpw(utilisateur.getMotDePasse(), BCrypt.gensalt());
        utilisateur.setMotDePasse(hashedPassword);
        
        // Générer un token de vérification
        String token = UUID.randomUUID().toString();
        utilisateur.setTokenVerification(token);
        
        // Créer l'utilisateur
        int userId = utilisateurDAO.create(utilisateur);
        
        if (userId > 0) {
            // Envoyer l'email de vérification (peut échouer si non configuré)
            boolean emailEnvoye = emailService.envoyerEmailVerification(utilisateur.getEmail(), token);
            // Retourner le token même si l'email n'a pas été envoyé
            return token;
        }
        
        return null;
    }
    
    /**
     * Vérifie un utilisateur avec son token
     * @param token Token de vérification
     * @return true si la vérification a réussi, false sinon
     */
    public boolean verifierUtilisateur(String token) {
        Utilisateur utilisateur = utilisateurDAO.findByToken(token);
        
        if (utilisateur != null && !utilisateur.isActif()) {
            return utilisateurDAO.activerUtilisateur(utilisateur.getId());
        }
        
        return false;
    }
    
    /**
     * Met à jour le profil d'un utilisateur
     */
    public boolean mettreAJourProfil(Utilisateur utilisateur) {
        return utilisateurDAO.update(utilisateur);
    }
    
    /**
     * Change le mot de passe d'un utilisateur
     * @param userId ID de l'utilisateur
     * @param ancienMotDePasse Ancien mot de passe en clair
     * @param nouveauMotDePasse Nouveau mot de passe en clair
     * @return true si le changement a réussi, false sinon
     */
    public boolean changerMotDePasse(int userId, String ancienMotDePasse, String nouveauMotDePasse) {
        Utilisateur utilisateur = utilisateurDAO.findById(userId);
        
        if (utilisateur != null && BCrypt.checkpw(ancienMotDePasse, utilisateur.getMotDePasse())) {
            String hashedPassword = BCrypt.hashpw(nouveauMotDePasse, BCrypt.gensalt());
            return utilisateurDAO.updatePassword(userId, hashedPassword);
        }
        
        return false;
    }
    
    /**
     * Trouve un utilisateur par son ID
     */
    public Utilisateur trouverParId(int id) {
        return utilisateurDAO.findById(id);
    }
}
