package com.example.forum_project.dao;

import com.example.forum_project.models.Utilisateur;
import com.example.forum_project.utils.DBConnection;

import java.sql.*;
import java.util.List;

/**
 * Data Access Object pour la gestion des utilisateurs
 */
public class UtilisateurDAO {
    
    /**
     * Trouve un utilisateur par son email
     */
    public Utilisateur findByEmail(String email) {
        String sql = "SELECT * FROM utilisateurs WHERE email = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUtilisateur(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'utilisateur par email : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Trouve un utilisateur par son ID
     */
    public Utilisateur findById(int id) {
        String sql = "SELECT * FROM utilisateurs WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUtilisateur(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'utilisateur par ID : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Trouve un utilisateur par son token de vérification
     */
    public Utilisateur findByToken(String token) {
        String sql = "SELECT * FROM utilisateurs WHERE token_verification = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUtilisateur(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'utilisateur par token : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Crée un nouvel utilisateur
     */
    public int create(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, role, actif, token_verification, bio) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getEmail());
            stmt.setString(4, utilisateur.getMotDePasse());
            stmt.setString(5, utilisateur.getRole());
            stmt.setBoolean(6, utilisateur.isActif());
            stmt.setString(7, utilisateur.getTokenVerification());
            stmt.setString(8, utilisateur.getBio());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * Met à jour un utilisateur
     */
    public boolean update(Utilisateur utilisateur) {
        String sql = "UPDATE utilisateurs SET nom = ?, prenom = ?, email = ?, bio = ?, photo_profil = ? WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getEmail());
            stmt.setString(4, utilisateur.getBio());
            stmt.setString(5, utilisateur.getPhotoProfil());
            stmt.setInt(6, utilisateur.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Met à jour le mot de passe d'un utilisateur
     */
    public boolean updatePassword(int userId, String hashedPassword) {
        String sql = "UPDATE utilisateurs SET mot_de_passe = ? WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, hashedPassword);
            stmt.setInt(2, userId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du mot de passe : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Active un utilisateur et supprime son token de vérification
     */
    public boolean activerUtilisateur(int userId) {
        String sql = "UPDATE utilisateurs SET actif = TRUE, token_verification = NULL WHERE id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'activation de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Vérifie si un email existe déjà
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM utilisateurs WHERE email = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'email : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Mappe un ResultSet vers un objet Utilisateur
     */
    private Utilisateur mapResultSetToUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(rs.getInt("id"));
        utilisateur.setNom(rs.getString("nom"));
        utilisateur.setPrenom(rs.getString("prenom"));
        utilisateur.setEmail(rs.getString("email"));
        utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
        utilisateur.setRole(rs.getString("role"));
        utilisateur.setActif(rs.getBoolean("actif"));
        utilisateur.setTokenVerification(rs.getString("token_verification"));
        utilisateur.setDateInscription(rs.getTimestamp("date_inscription"));
        utilisateur.setPhotoProfil(rs.getString("photo_profil"));
        utilisateur.setBio(rs.getString("bio"));
        return utilisateur;
    }
}
