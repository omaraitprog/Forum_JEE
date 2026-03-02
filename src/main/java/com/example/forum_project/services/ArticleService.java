package com.example.forum_project.services;

import com.example.forum_project.dao.ArticleDAO;
import com.example.forum_project.models.Article;

import java.util.List;

/**
 * Service pour la gestion des articles
 * Contient la logique métier liée aux articles
 */
public class ArticleService {
    private ArticleDAO articleDAO;
    
    public ArticleService() {
        this.articleDAO = new ArticleDAO();
    }
    
    /**
     * Récupère tous les articles publiés avec pagination
     */
    public List<Article> trouverTous(int page, int limit) {
        return articleDAO.findAll(page, limit);
    }
    
    /**
     * Trouve un article par son ID
     */
    public Article trouverParId(int id) {
        return articleDAO.findById(id);
    }
    
    /**
     * Trouve tous les articles d'un auteur
     */
    public List<Article> trouverParAuteur(int auteurId) {
        return articleDAO.findByAuteur(auteurId);
    }
    
    /**
     * Crée un nouvel article
     */
    public int creer(Article article) {
        return articleDAO.create(article);
    }
    
    /**
     * Met à jour un article
     */
    public boolean mettreAJour(Article article) {
        return articleDAO.update(article);
    }
    
    /**
     * Supprime un article
     */
    public boolean supprimer(int id) {
        return articleDAO.delete(id);
    }
    
    /**
     * Compte le nombre total d'articles publiés
     */
    public int compterTous() {
        return articleDAO.countAll();
    }
    
    /**
     * Calcule le nombre total de pages pour la pagination
     */
    public int calculerNombrePages(int limit) {
        int total = compterTous();
        return (int) Math.ceil((double) total / limit);
    }
}
