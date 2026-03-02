package com.example.forum_project.dao;

import com.example.forum_project.models.Commentaire;
import com.example.forum_project.models.Utilisateur;
import com.example.forum_project.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object pour la gestion des commentaires
 */
public class CommentaireDAO {
    
    /**
     * Trouve tous les commentaires d'un article
     */
    public List<Commentaire> findByArticle(int articleId) {
        List<Commentaire> commentaires = new ArrayList<>();
        String sql = "SELECT c.*, u.nom, u.prenom, u.email " +
                     "FROM commentaires c " +
                     "JOIN utilisateurs u ON c.auteur_id = u.id " +
                     "WHERE c.article_id = ? AND c.approuve = TRUE " +
                     "ORDER BY c.date_creation ASC";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, articleId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Commentaire commentaire = mapResultSetToCommentaire(rs);
                commentaires.add(commentaire);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des commentaires : " + e.getMessage());
            e.printStackTrace();
        }
        return commentaires;
    }
    
    /**
     * Crée un nouveau commentaire
     */
    public int create(Commentaire commentaire) {
        String sql = "INSERT INTO commentaires (contenu, article_id, auteur_id, approuve) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, commentaire.getContenu());
            stmt.setInt(2, commentaire.getArticleId());
            stmt.setInt(3, commentaire.getAuteurId());
            stmt.setBoolean(4, commentaire.isApprouve());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du commentaire : " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * Supprime un commentaire
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM commentaires WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du commentaire : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Trouve un commentaire par son ID
     */
    public Commentaire findById(int id) {
        String sql = "SELECT c.*, u.nom, u.prenom, u.email " +
                     "FROM commentaires c " +
                     "JOIN utilisateurs u ON c.auteur_id = u.id " +
                     "WHERE c.id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCommentaire(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du commentaire : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Mappe un ResultSet vers un objet Commentaire avec l'auteur
     */
    private Commentaire mapResultSetToCommentaire(ResultSet rs) throws SQLException {
        Commentaire commentaire = new Commentaire();
        commentaire.setId(rs.getInt("id"));
        commentaire.setContenu(rs.getString("contenu"));
        commentaire.setArticleId(rs.getInt("article_id"));
        commentaire.setAuteurId(rs.getInt("auteur_id"));
        commentaire.setDateCreation(rs.getTimestamp("date_creation"));
        commentaire.setApprouve(rs.getBoolean("approuve"));
        
        // Créer l'objet auteur
        Utilisateur auteur = new Utilisateur();
        auteur.setId(rs.getInt("auteur_id"));
        auteur.setNom(rs.getString("nom"));
        auteur.setPrenom(rs.getString("prenom"));
        auteur.setEmail(rs.getString("email"));
        commentaire.setAuteur(auteur);
        
        return commentaire;
    }
}
