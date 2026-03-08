package com.example.forum_project.controllers;

import com.example.forum_project.models.Utilisateur;
import com.example.forum_project.services.UtilisateurService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet pour l'inscription des nouveaux utilisateurs
 */
@WebServlet(name = "InscriptionServlet", urlPatterns = "/inscription")
public class InscriptionServlet extends HttpServlet {
    private UtilisateurService utilisateurService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        utilisateurService = new UtilisateurService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        String motDePasse = request.getParameter("motDePasse");
        String confirmation = request.getParameter("confirmation");
        String bio = request.getParameter("bio");
        
        // Validation des champs
        if (nom == null || nom.trim().isEmpty() ||
            prenom == null || prenom.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            motDePasse == null || motDePasse.trim().isEmpty() ||
            confirmation == null || confirmation.trim().isEmpty()) {
            
            request.setAttribute("erreur", "Veuillez remplir tous les champs obligatoires");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }
        
        // Vérifier que les mots de passe correspondent
        if (!motDePasse.equals(confirmation)) {
            request.setAttribute("erreur", "Les mots de passe ne correspondent pas");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }
        
        // Vérifier la longueur du mot de passe
        if (motDePasse.length() < 6) {
            request.setAttribute("erreur", "Le mot de passe doit contenir au moins 6 caractères");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }
        
        // Vérifier que l'email n'existe pas déjà
        try {
            com.example.forum_project.dao.UtilisateurDAO dao = new com.example.forum_project.dao.UtilisateurDAO();
            boolean emailExists = dao.emailExists(email);
            if (emailExists) {
                request.setAttribute("erreur", "Cet email est déjà utilisé");
                request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
                return;
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification de l'email: " + email);
            System.err.println("Type d'erreur: " + e.getClass().getName());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("erreur", "Erreur de connexion à la base de données. Veuillez réessayer plus tard.");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }
        
        // Créer l'utilisateur
        try {
            Utilisateur utilisateur = new Utilisateur(nom, prenom, email, motDePasse);
            if (bio != null && !bio.trim().isEmpty()) {
                utilisateur.setBio(bio);
            }
            
            System.out.println("Tentative d'inscription pour: " + email);
            String token = utilisateurService.inscrire(utilisateur);
            
            if (token != null) {
                // Construire le lien de vérification
                String baseUrl = request.getScheme() + "://" + request.getServerName();
                // Ne pas inclure le port si c'est le port standard (80 pour HTTP, 443 pour HTTPS)
                int port = request.getServerPort();
                if (port != 80 && port != 443) {
                    baseUrl += ":" + port;
                }
                baseUrl += request.getContextPath();
                String verificationLink = baseUrl + "/verifier?token=" + token;
                
                System.out.println("Inscription réussie pour: " + email);
                request.setAttribute("succes", "Inscription réussie !");
                request.setAttribute("verificationLink", verificationLink);
                request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            } else {
                System.err.println("ERREUR CRITIQUE: utilisateurService.inscrire() a retourné null pour l'email: " + email);
                System.err.println("Cela signifie que l'inscription a échoué dans UtilisateurService");
                request.setAttribute("erreur", "Erreur lors de l'inscription. Veuillez réessayer. Si le problème persiste, vérifiez les logs du serveur.");
                request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("EXCEPTION lors de l'inscription pour l'email: " + email);
            System.err.println("Type d'erreur: " + e.getClass().getName());
            System.err.println("Message: " + e.getMessage());
            System.err.println("Cause: " + (e.getCause() != null ? e.getCause().getMessage() : "N/A"));
            e.printStackTrace();
            String errorMsg = "Erreur lors de l'inscription: " + e.getMessage();
            // En production, ne pas exposer les détails de l'erreur
            if (errorMsg.length() > 100) {
                errorMsg = "Erreur lors de l'inscription. Veuillez réessayer.";
            }
            request.setAttribute("erreur", errorMsg);
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
        }
    }
}
