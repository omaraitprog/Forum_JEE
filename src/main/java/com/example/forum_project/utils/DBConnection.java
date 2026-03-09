package com.example.forum_project.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitaire pour la gestion de la connexion à la base de données MySQL
 * Implémente le pattern Singleton pour une seule instance de connexion
 */
public class DBConnection {
    private static DBConnection instance;
    private static Connection connection;
    
    // Paramètres de connexion à la base de données MySQL
    // Modifiez ces valeurs selon votre configuration locale
    private static final String DB_HOST = getEnvOrDefault("DB_HOST", "localhost");
    private static final String DB_PORT = getEnvOrDefault("DB_PORT", "3306");
    private static final String DB_NAME = getEnvOrDefault("DB_NAME", "blog_jee");
    private static final String DB_USER = getEnvOrDefault("DB_USER", "root");
    private static final String DB_PASSWORD = getEnvOrDefault("DB_PASSWORD", "");
    
    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    static {
        System.out.println("=== DBConnection Initialization ===");
        System.out.println("URL de la base de données: " + DB_URL);
        System.out.println("Utilisateur: " + DB_USER);
        System.out.println("Base de données: " + DB_NAME);
    }
    
    /**
     * Récupère une variable d'environnement ou retourne une valeur par défaut
     */
    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }
    
    /**
     * Constructeur privé pour empêcher l'instanciation directe
     */
    private DBConnection() {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur : Driver MySQL non trouvé");
            e.printStackTrace();
        }
    }
    
    /**
     * Retourne l'instance unique de DBConnection (Singleton)
     * @return instance de DBConnection
     */
    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }
    
    /**
     * Obtient une connexion à la base de données
     * @return Connection à la base de données
     * @throws SQLException si une erreur survient lors de la connexion
     */
    public Connection getConnection() throws SQLException {
        try {
            // Vérifier si la connexion existe et est valide
            if (connection != null && !connection.isClosed()) {
                // Tester si la connexion est toujours valide
                try {
                    if (connection.isValid(2)) { // Timeout de 2 secondes
                        return connection;
                    }
                } catch (SQLException e) {
                    System.err.println("La connexion n'est plus valide, création d'une nouvelle connexion");
                    connection = null;
                }
            }
            
            // Créer une nouvelle connexion
            System.out.println("Création d'une nouvelle connexion à la base de données: " + DB_URL);
            
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connexion établie avec succès");
            
            return connection;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données: " + DB_URL);
            System.err.println("Message d'erreur: " + e.getMessage());
            System.err.println("Code d'erreur SQL: " + e.getErrorCode());
            System.err.println("État SQL: " + e.getSQLState());
            connection = null; // Réinitialiser la connexion en cas d'erreur
            throw e;
        }
    }
    
    /**
     * Ferme la connexion à la base de données
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion");
            e.printStackTrace();
        }
    }
}
