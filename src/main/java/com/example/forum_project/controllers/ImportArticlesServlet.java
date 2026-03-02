package com.example.forum_project.controllers;

import com.example.forum_project.models.Utilisateur;
import com.example.forum_project.services.ImportArticleService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet pour gérer l'importation des cours depuis mazoul.online
 * Accessible uniquement aux administrateurs
 */
@WebServlet("/admin/import-articles")
public class ImportArticlesServlet extends HttpServlet {
    
    private ImportArticleService importService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.importService = new ImportArticleService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Vérifier que l'utilisateur est connecté et est admin
        Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("utilisateur");
        
        if (utilisateur == null || !utilisateur.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé. Administrateur requis.");
            return;
        }
        
        // Afficher la page d'importation
        request.getRequestDispatcher("/WEB-INF/views/admin/import-articles.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Vérifier que l'utilisateur est connecté et est admin
        Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("utilisateur");
        
        if (utilisateur == null || !utilisateur.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé. Administrateur requis.");
            return;
        }
        
        String action = request.getParameter("action");
        String url = request.getParameter("url");
        
        List<Integer> articlesCrees = null;
        String message = "";
        boolean success = false;
        
        try {
            if ("import-all".equals(action)) {
                // Importer tous les cours depuis mazoul.online
                articlesCrees = importService.importerTousLesCours(utilisateur.getId());
                message = articlesCrees.size() + " article(s) importé(s) avec succès depuis mazoul.online.";
                success = true;
                
            } else if ("import-url".equals(action) && url != null && !url.trim().isEmpty()) {
                // Importer depuis une URL spécifique
                articlesCrees = importService.importerCoursDepuisUrl(url, utilisateur.getId());
                message = articlesCrees.size() + " article(s) importé(s) depuis l'URL spécifiée.";
                success = true;
                
            } else {
                message = "Action invalide ou URL manquante.";
            }
            
        } catch (Exception e) {
            message = "Erreur lors de l'importation: " + e.getMessage();
            e.printStackTrace();
        }
        
        // Rediriger vers la page d'importation avec un message
        request.setAttribute("message", message);
        request.setAttribute("success", success);
        request.setAttribute("articlesCrees", articlesCrees != null ? articlesCrees.size() : 0);
        
        request.getRequestDispatcher("/WEB-INF/views/admin/import-articles.jsp").forward(request, response);
    }
}
