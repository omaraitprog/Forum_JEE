package com.example.forum_project.dao;

import com.example.forum_project.models.Article;
import com.example.forum_project.models.Utilisateur;
import com.example.forum_project.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object pour la gestion des articles
 */
public class ArticleDAO {
    
    /**
     * Trouve tous les articles publiés avec pagination
     */
    public List<Article> findAll(int page, int limit) {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT a.*, u.nom, u.prenom, u.email " +
                     "FROM articles a " +
                     "JOIN utilisateurs u ON a.auteur_id = u.id " +
                     "WHERE a.statut = 'PUBLIE' " +
                     "ORDER BY a.date_creation DESC " +
                     "LIMIT ? OFFSET ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            int offset = (page - 1) * limit;
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Article article = mapResultSetToArticle(rs);
                articles.add(article);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des articles : " + e.getMessage());
            e.printStackTrace();
        }
        return articles;
    }
    
    /**
     * Trouve un article par son ID
     */
    public Article findById(int id) {
        String sql = "SELECT a.*, u.nom, u.prenom, u.email " +
                     "FROM articles a " +
                     "JOIN utilisateurs u ON a.auteur_id = u.id " +
                     "WHERE a.id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToArticle(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'article : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Trouve tous les articles d'un auteur
     */
    public List<Article> findByAuteur(int auteurId) {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT a.*, u.nom, u.prenom, u.email " +
                     "FROM articles a " +
                     "JOIN utilisateurs u ON a.auteur_id = u.id " +
                     "WHERE a.auteur_id = ? " +
                     "ORDER BY a.date_creation DESC";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, auteurId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Article article = mapResultSetToArticle(rs);
                articles.add(article);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des articles de l'auteur : " + e.getMessage());
            e.printStackTrace();
        }
        return articles;
    }
    
    /**
     * Crée un nouvel article
     */
    public int create(Article article) {
        String sql = "INSERT INTO articles (titre, contenu, resume, auteur_id, statut, image_url) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, article.getTitre());
            stmt.setString(2, article.getContenu());
            stmt.setString(3, article.getResume());
            stmt.setInt(4, article.getAuteurId());
            stmt.setString(5, article.getStatut());
            stmt.setString(6, article.getImageUrl());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'article : " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * Met à jour un article
     */
    public boolean update(Article article) {
        String sql = "UPDATE articles SET titre = ?, contenu = ?, resume = ?, statut = ?, " +
                     "image_url = ?, date_modification = NOW() WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, article.getTitre());
            stmt.setString(2, article.getContenu());
            stmt.setString(3, article.getResume());
            stmt.setString(4, article.getStatut());
            stmt.setString(5, article.getImageUrl());
            stmt.setInt(6, article.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'article : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Supprime un article
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM articles WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'article : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Compte le nombre total d'articles publiés
     */
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM articles WHERE statut = 'PUBLIE'";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des articles : " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Mappe un ResultSet vers un objet Article avec l'auteur
     */
    private Article mapResultSetToArticle(ResultSet rs) throws SQLException {
        Article article = new Article();
        article.setId(rs.getInt("id"));
        article.setTitre(rs.getString("titre"));
        article.setContenu(rs.getString("contenu"));
        article.setResume(rs.getString("resume"));
        article.setAuteurId(rs.getInt("auteur_id"));
        article.setDateCreation(rs.getTimestamp("date_creation"));
        article.setDateModification(rs.getTimestamp("date_modification"));
        article.setStatut(rs.getString("statut"));
        article.setImageUrl(rs.getString("image_url"));
        
        // Créer l'objet auteur
        Utilisateur auteur = new Utilisateur();
        auteur.setId(rs.getInt("auteur_id"));
        auteur.setNom(rs.getString("nom"));
        auteur.setPrenom(rs.getString("prenom"));
        auteur.setEmail(rs.getString("email"));
        article.setAuteur(auteur);
        
        return article;
    }
}
