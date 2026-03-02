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
 * Servlet pour la gestion de l'authentification (login/logout)
 */
@WebServlet(name = "AuthServlet", urlPatterns = {"/login", "/logout"})
public class AuthServlet extends HttpServlet {
    private UtilisateurService utilisateurService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        utilisateurService = new UtilisateurService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getServletPath();
        
        if ("/logout".equals(path)) {
            // Déconnexion
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/articles");
        } else {
            // Affichage de la page de connexion
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getServletPath();
        
        if ("/login".equals(path)) {
            String email = request.getParameter("email");
            String motDePasse = request.getParameter("motDePasse");
            
            if (email == null || email.trim().isEmpty() || 
                motDePasse == null || motDePasse.trim().isEmpty()) {
                request.setAttribute("erreur", "Veuillez remplir tous les champs");
                request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
                return;
            }
            
            Utilisateur utilisateur = utilisateurService.authentifier(email, motDePasse);
            
            if (utilisateur != null) {
                // Créer une session
                HttpSession session = request.getSession();
                session.setAttribute("utilisateur", utilisateur);
                session.setMaxInactiveInterval(30 * 60); // 30 minutes
                
                // Rediriger vers la liste des articles
                response.sendRedirect(request.getContextPath() + "/articles");
            } else {
                request.setAttribute("erreur", "Email ou mot de passe incorrect, ou compte non activé");
                request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            }
        }
    }
}
