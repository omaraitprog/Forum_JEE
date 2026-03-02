package com.example.forum_project.controllers;

import com.example.forum_project.services.UtilisateurService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet pour la vérification de l'email lors de l'inscription
 */
@WebServlet(name = "VerificationEmailServlet", urlPatterns = "/verifier")
public class VerificationEmailServlet extends HttpServlet {
    private UtilisateurService utilisateurService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        utilisateurService = new UtilisateurService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String token = request.getParameter("token");
        
        if (token == null || token.trim().isEmpty()) {
            request.setAttribute("erreur", "Token de vérification invalide");
            request.getRequestDispatcher("/WEB-INF/views/auth/verify-email.jsp").forward(request, response);
            return;
        }
        
        boolean success = utilisateurService.verifierUtilisateur(token);
        
        if (success) {
            request.setAttribute("succes", "Votre compte a été activé avec succès ! Vous pouvez maintenant vous connecter.");
        } else {
            request.setAttribute("erreur", "Token de vérification invalide ou expiré");
        }
        
        request.getRequestDispatcher("/WEB-INF/views/auth/verify-email.jsp").forward(request, response);
    }
}
