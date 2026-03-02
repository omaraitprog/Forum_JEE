package com.example.forum_project.controllers;

import com.example.forum_project.models.Utilisateur;
import com.example.forum_project.services.UtilisateurService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet pour la gestion du profil utilisateur
 */
@WebServlet(name = "ProfilServlet", urlPatterns = "/profil")
public class ProfilServlet extends HttpServlet {
    private UtilisateurService utilisateurService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        utilisateurService = new UtilisateurService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utilisateur") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        // Récupérer les données à jour depuis la base
        utilisateur = utilisateurService.trouverParId(utilisateur.getId());
        request.setAttribute("utilisateur", utilisateur);
        request.getRequestDispatcher("/WEB-INF/views/profile/profile.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utilisateur") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        utilisateur = utilisateurService.trouverParId(utilisateur.getId());
        
        String action = request.getParameter("action");
        
        if ("change-password".equals(action)) {
            // Changement de mot de passe
            String ancienMotDePasse = request.getParameter("ancienMotDePasse");
            String nouveauMotDePasse = request.getParameter("nouveauMotDePasse");
            String confirmation = request.getParameter("confirmation");
            
            if (ancienMotDePasse == null || ancienMotDePasse.trim().isEmpty() ||
                nouveauMotDePasse == null || nouveauMotDePasse.trim().isEmpty() ||
                confirmation == null || confirmation.trim().isEmpty()) {
                request.setAttribute("erreur", "Veuillez remplir tous les champs");
                request.setAttribute("utilisateur", utilisateur);
                request.getRequestDispatcher("/WEB-INF/views/profile/profile.jsp").forward(request, response);
                return;
            }
            
            if (!nouveauMotDePasse.equals(confirmation)) {
                request.setAttribute("erreur", "Les nouveaux mots de passe ne correspondent pas");
                request.setAttribute("utilisateur", utilisateur);
                request.getRequestDispatcher("/WEB-INF/views/profile/profile.jsp").forward(request, response);
                return;
            }
            
            if (nouveauMotDePasse.length() < 6) {
                request.setAttribute("erreur", "Le nouveau mot de passe doit contenir au moins 6 caractères");
                request.setAttribute("utilisateur", utilisateur);
                request.getRequestDispatcher("/WEB-INF/views/profile/profile.jsp").forward(request, response);
                return;
            }
            
            boolean success = utilisateurService.changerMotDePasse(
                utilisateur.getId(), 
                ancienMotDePasse, 
                nouveauMotDePasse
            );
            
            if (success) {
                request.setAttribute("succes", "Mot de passe modifié avec succès");
            } else {
                request.setAttribute("erreur", "Ancien mot de passe incorrect");
            }
            
            utilisateur = utilisateurService.trouverParId(utilisateur.getId());
            request.setAttribute("utilisateur", utilisateur);
            request.getRequestDispatcher("/WEB-INF/views/profile/profile.jsp").forward(request, response);
        } else {
            // Mise à jour du profil
            String nom = request.getParameter("nom");
            String prenom = request.getParameter("prenom");
            String email = request.getParameter("email");
            String bio = request.getParameter("bio");
            String photoProfil = request.getParameter("photoProfil");
            
            if (nom == null || nom.trim().isEmpty() ||
                prenom == null || prenom.trim().isEmpty() ||
                email == null || email.trim().isEmpty()) {
                request.setAttribute("erreur", "Les champs nom, prénom et email sont obligatoires");
                request.setAttribute("utilisateur", utilisateur);
                request.getRequestDispatcher("/WEB-INF/views/profile/profile.jsp").forward(request, response);
                return;
            }
            
            utilisateur.setNom(nom);
            utilisateur.setPrenom(prenom);
            utilisateur.setEmail(email);
            utilisateur.setBio(bio);
            utilisateur.setPhotoProfil(photoProfil);
            
            boolean success = utilisateurService.mettreAJourProfil(utilisateur);
            
            if (success) {
                // Mettre à jour la session
                utilisateur = utilisateurService.trouverParId(utilisateur.getId());
                session.setAttribute("utilisateur", utilisateur);
                request.setAttribute("succes", "Profil mis à jour avec succès");
            } else {
                request.setAttribute("erreur", "Erreur lors de la mise à jour du profil");
            }
            
            request.setAttribute("utilisateur", utilisateur);
            request.getRequestDispatcher("/WEB-INF/views/profile/profile.jsp").forward(request, response);
        }
    }
}
