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
        com.example.forum_project.dao.UtilisateurDAO dao = new com.example.forum_project.dao.UtilisateurDAO();
        if (dao.emailExists(email)) {
            request.setAttribute("erreur", "Cet email est déjà utilisé");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }
        
        // Créer l'utilisateur
        Utilisateur utilisateur = new Utilisateur(nom, prenom, email, motDePasse);
        if (bio != null && !bio.trim().isEmpty()) {
            utilisateur.setBio(bio);
        }
        
        String token = utilisateurService.inscrire(utilisateur);
        
        if (token != null) {
            // Construire le lien de vérification
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + 
                           request.getServerPort() + request.getContextPath();
            String verificationLink = baseUrl + "/verifier?token=" + token;
            
            request.setAttribute("succes", "Inscription réussie !");
            request.setAttribute("verificationLink", verificationLink);
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
        } else {
            request.setAttribute("erreur", "Erreur lors de l'inscription. Veuillez réessayer.");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
        }
    }
}
