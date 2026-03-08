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
        try {
            System.out.println("Début de l'inscription pour l'email: " + utilisateur.getEmail());
            
            // Vérifier que l'email n'existe pas déjà
            boolean emailExists = false;
            try {
                emailExists = utilisateurDAO.emailExists(utilisateur.getEmail());
                System.out.println("Vérification email - existe déjà: " + emailExists);
            } catch (Exception e) {
                System.err.println("Erreur lors de la vérification de l'email: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
            
            if (emailExists) {
                System.err.println("L'email existe déjà: " + utilisateur.getEmail());
                return null;
            }
            
            // Hasher le mot de passe avec BCrypt
            String hashedPassword;
            try {
                hashedPassword = BCrypt.hashpw(utilisateur.getMotDePasse(), BCrypt.gensalt());
                utilisateur.setMotDePasse(hashedPassword);
                System.out.println("Mot de passe hashé avec succès");
            } catch (Exception e) {
                System.err.println("Erreur lors du hashage du mot de passe: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
            
            // Générer un token de vérification
            String token = UUID.randomUUID().toString();
            utilisateur.setTokenVerification(token);
            System.out.println("Token généré: " + token);
            
            // Créer l'utilisateur
            int userId = utilisateurDAO.create(utilisateur);
            System.out.println("Résultat de la création de l'utilisateur - userId: " + userId);
            
            if (userId > 0) {
                // Envoyer l'email de vérification (peut échouer si non configuré)
                try {
                    boolean emailEnvoye = emailService.envoyerEmailVerification(utilisateur.getEmail(), token);
                    System.out.println("Email de vérification envoyé: " + emailEnvoye);
                } catch (Exception e) {
                    System.err.println("Erreur lors de l'envoi de l'email (non bloquant): " + e.getMessage());
                    // Ne pas bloquer l'inscription si l'email échoue
                }
                // Retourner le token même si l'email n'a pas été envoyé
                System.out.println("Inscription réussie pour l'email: " + utilisateur.getEmail());
                return token;
            } else {
                System.err.println("Échec de la création de l'utilisateur - userId invalide: " + userId);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Erreur inattendue lors de l'inscription pour l'email: " + utilisateur.getEmail());
            System.err.println("Type d'erreur: " + e.getClass().getName());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
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
    
    /**
     * Hashe un mot de passe avec BCrypt
     */
    public String hashPassword(String motDePasse) {
        return BCrypt.hashpw(motDePasse, BCrypt.gensalt());
    }
}
