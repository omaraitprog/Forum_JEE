package com.example.forum_project.controllers;

import com.example.forum_project.dao.ArticleDAO;
import com.example.forum_project.dao.UtilisateurDAO;
import com.example.forum_project.models.Article;
import com.example.forum_project.models.Utilisateur;
import com.example.forum_project.services.UtilisateurService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet d'administration pour ajouter des données dans la base de données
 * Accessible uniquement aux administrateurs
 */
@WebServlet("/admin/add-data")
public class AdminDataServlet extends HttpServlet {
    
    private UtilisateurDAO utilisateurDAO;
    private ArticleDAO articleDAO;
    private UtilisateurService utilisateurService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.utilisateurDAO = new UtilisateurDAO();
        this.articleDAO = new ArticleDAO();
        this.utilisateurService = new UtilisateurService();
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
        
        // Récupérer la liste des utilisateurs pour les sélections
        List<Utilisateur> utilisateurs = utilisateurDAO.findAll();
        request.setAttribute("utilisateurs", utilisateurs);
        
        // Afficher la page d'administration
        request.getRequestDispatcher("/WEB-INF/views/admin/add-data.jsp").forward(request, response);
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
        String message = "";
        boolean success = false;
        
        try {
            if ("add-user".equals(action)) {
                // Ajouter un nouvel utilisateur
                String nom = request.getParameter("nom");
                String prenom = request.getParameter("prenom");
                String email = request.getParameter("email");
                String motDePasse = request.getParameter("mot_de_passe");
                String role = request.getParameter("role");
                String bio = request.getParameter("bio");
                
                if (nom == null || prenom == null || email == null || motDePasse == null) {
                    message = "Tous les champs obligatoires doivent être remplis.";
                } else if (utilisateurDAO.emailExists(email)) {
                    message = "Cet email est déjà utilisé.";
                } else {
                    Utilisateur newUser = new Utilisateur(nom, prenom, email, motDePasse);
                    newUser.setRole(role != null ? role : "MEMBRE");
                    newUser.setActif(true);
                    newUser.setBio(bio);
                    
                    // Hasher le mot de passe
                    String hashedPassword = utilisateurService.hashPassword(motDePasse);
                    newUser.setMotDePasse(hashedPassword);
                    
                    int userId = utilisateurDAO.create(newUser);
                    if (userId > 0) {
                        message = "Utilisateur créé avec succès (ID: " + userId + ")";
                        success = true;
                    } else {
                        message = "Erreur lors de la création de l'utilisateur.";
                    }
                }
                
            } else if ("add-article".equals(action)) {
                // Ajouter un nouvel article
                String titre = request.getParameter("titre");
                String contenu = request.getParameter("contenu");
                String resume = request.getParameter("resume");
                String auteurIdStr = request.getParameter("auteur_id");
                String statut = request.getParameter("statut");
                String imageUrl = request.getParameter("image_url");
                
                if (titre == null || contenu == null || auteurIdStr == null) {
                    message = "Titre, contenu et auteur sont obligatoires.";
                } else {
                    try {
                        int auteurId = Integer.parseInt(auteurIdStr);
                        Article article = new Article(titre, contenu, auteurId);
                        article.setResume(resume);
                        article.setStatut(statut != null ? statut : "PUBLIE");
                        article.setImageUrl(imageUrl);
                        
                        int articleId = articleDAO.create(article);
                        if (articleId > 0) {
                            message = "Article créé avec succès (ID: " + articleId + ")";
                            success = true;
                        } else {
                            message = "Erreur lors de la création de l'article.";
                        }
                    } catch (NumberFormatException e) {
                        message = "ID auteur invalide.";
                    }
                }
            } else {
                message = "Action invalide.";
            }
            
        } catch (Exception e) {
            message = "Erreur: " + e.getMessage();
            e.printStackTrace();
        }
        
        // Récupérer la liste des utilisateurs pour les sélections
        List<Utilisateur> utilisateurs = utilisateurDAO.findAll();
        request.setAttribute("utilisateurs", utilisateurs);
        
        request.setAttribute("message", message);
        request.setAttribute("success", success);
        request.getRequestDispatcher("/WEB-INF/views/admin/add-data.jsp").forward(request, response);
    }
}
