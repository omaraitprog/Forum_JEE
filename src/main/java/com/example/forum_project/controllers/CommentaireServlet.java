package com.example.forum_project.controllers;

import com.example.forum_project.dao.CommentaireDAO;
import com.example.forum_project.models.Commentaire;
import com.example.forum_project.models.Utilisateur;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet pour la gestion des commentaires
 */
@WebServlet(name = "CommentaireServlet", urlPatterns = "/commentaires")
public class CommentaireServlet extends HttpServlet {
    private CommentaireDAO commentaireDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        commentaireDAO = new CommentaireDAO();
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
        String method = request.getParameter("_method");
        
        if ("DELETE".equals(method)) {
            // Suppression d'un commentaire
            String idParam = request.getParameter("id");
            if (idParam != null) {
                try {
                    int id = Integer.parseInt(idParam);
                    Commentaire commentaire = commentaireDAO.findById(id);
                    
                    if (commentaire != null && 
                        (commentaire.getAuteurId() == utilisateur.getId() || utilisateur.isAdmin())) {
                        commentaireDAO.delete(id);
                    }
                    
                    // Rediriger vers l'article
                    String articleId = request.getParameter("articleId");
                    if (articleId != null) {
                        response.sendRedirect(request.getContextPath() + "/articles?id=" + articleId);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/articles");
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        } else {
            // Création d'un nouveau commentaire
            String contenu = request.getParameter("contenu");
            String articleIdParam = request.getParameter("articleId");
            
            if (contenu == null || contenu.trim().isEmpty() || 
                articleIdParam == null || articleIdParam.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            
            try {
                int articleId = Integer.parseInt(articleIdParam);
                Commentaire commentaire = new Commentaire(contenu, articleId, utilisateur.getId());
                
                int commentaireId = commentaireDAO.create(commentaire);
                
                if (commentaireId > 0) {
                    response.sendRedirect(request.getContextPath() + "/articles?id=" + articleId);
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }
}
