package com.example.forum_project.controllers;

import com.example.forum_project.models.Article;
import com.example.forum_project.models.Utilisateur;
import com.example.forum_project.services.ArticleService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet pour la gestion des articles (CRUD)
 */
@WebServlet(name = "ArticlesServlet", urlPatterns = "/articles")
public class ArticlesServlet extends HttpServlet {
    private ArticleService articleService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        articleService = new ArticleService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        String action = request.getParameter("action");
        
        if ("create".equals(action)) {
            // Afficher le formulaire de création
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("utilisateur") == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            request.getRequestDispatcher("/WEB-INF/views/articles/create.jsp").forward(request, response);
        } else if ("edit".equals(action) && idParam != null) {
            // Afficher le formulaire d'édition
            HttpSession session = request.getSession(false);
            Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
            
            if (utilisateur == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            
            try {
                int id = Integer.parseInt(idParam);
                Article article = articleService.trouverParId(id);
                
                if (article != null && (article.getAuteurId() == utilisateur.getId() || utilisateur.isAdmin())) {
                    request.setAttribute("article", article);
                    request.getRequestDispatcher("/WEB-INF/views/articles/edit.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else if (idParam != null) {
            // Afficher le détail d'un article
            try {
                int id = Integer.parseInt(idParam);
                Article article = articleService.trouverParId(id);
                
                if (article != null && article.isPublie()) {
                    // Charger les commentaires
                    com.example.forum_project.dao.CommentaireDAO commentaireDAO = new com.example.forum_project.dao.CommentaireDAO();
                    java.util.List<com.example.forum_project.models.Commentaire> commentaires = commentaireDAO.findByArticle(id);
                    
                    request.setAttribute("article", article);
                    request.setAttribute("commentaires", commentaires);
                    request.getRequestDispatcher("/WEB-INF/views/articles/detail.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            // Liste des articles avec pagination
            int page = 1;
            int limit = 10;
            
            try {
                String pageParam = request.getParameter("page");
                if (pageParam != null) {
                    page = Integer.parseInt(pageParam);
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
            
            List<Article> articles = articleService.trouverTous(page, limit);
            int totalPages = articleService.calculerNombrePages(limit);
            
            request.setAttribute("articles", articles);
            request.setAttribute("page", page);
            request.setAttribute("totalPages", totalPages);
            request.getRequestDispatcher("/WEB-INF/views/articles/list.jsp").forward(request, response);
        }
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
        
        if ("PUT".equals(method)) {
            // Mise à jour d'un article
            String idParam = request.getParameter("id");
            if (idParam != null) {
                try {
                    int id = Integer.parseInt(idParam);
                    Article article = articleService.trouverParId(id);
                    
                    if (article != null && (article.getAuteurId() == utilisateur.getId() || utilisateur.isAdmin())) {
                        article.setTitre(request.getParameter("titre"));
                        article.setContenu(request.getParameter("contenu"));
                        article.setResume(request.getParameter("resume"));
                        article.setStatut(request.getParameter("statut"));
                        article.setImageUrl(request.getParameter("imageUrl"));
                        
                        if (articleService.mettreAJour(article)) {
                            response.sendRedirect(request.getContextPath() + "/articles?id=" + id);
                        } else {
                            request.setAttribute("erreur", "Erreur lors de la mise à jour");
                            request.setAttribute("article", article);
                            request.getRequestDispatcher("/WEB-INF/views/articles/edit.jsp").forward(request, response);
                        }
                    } else {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        } else if ("DELETE".equals(method)) {
            // Suppression d'un article
            String idParam = request.getParameter("id");
            if (idParam != null) {
                try {
                    int id = Integer.parseInt(idParam);
                    Article article = articleService.trouverParId(id);
                    
                    if (article != null && (article.getAuteurId() == utilisateur.getId() || utilisateur.isAdmin())) {
                        articleService.supprimer(id);
                    }
                    response.sendRedirect(request.getContextPath() + "/articles");
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        } else {
            // Création d'un nouvel article
            String titre = request.getParameter("titre");
            String contenu = request.getParameter("contenu");
            String resume = request.getParameter("resume");
            String statut = request.getParameter("statut");
            String imageUrl = request.getParameter("imageUrl");
            
            if (titre == null || titre.trim().isEmpty() || 
                contenu == null || contenu.trim().isEmpty()) {
                request.setAttribute("erreur", "Le titre et le contenu sont obligatoires");
                request.getRequestDispatcher("/WEB-INF/views/articles/create.jsp").forward(request, response);
                return;
            }
            
            if (statut == null || statut.trim().isEmpty()) {
                statut = "PUBLIE";
            }
            
            Article article = new Article(titre, contenu, utilisateur.getId());
            article.setResume(resume);
            article.setStatut(statut);
            article.setImageUrl(imageUrl);
            
            int articleId = articleService.creer(article);
            
            if (articleId > 0) {
                response.sendRedirect(request.getContextPath() + "/articles?id=" + articleId);
            } else {
                request.setAttribute("erreur", "Erreur lors de la création de l'article");
                request.getRequestDispatcher("/WEB-INF/views/articles/create.jsp").forward(request, response);
            }
        }
    }
}
