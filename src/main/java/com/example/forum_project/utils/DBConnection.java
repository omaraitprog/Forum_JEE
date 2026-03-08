package com.example.forum_project.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitaire pour la gestion de la connexion à la base de données SQLite
 * Implémente le pattern Singleton pour une seule instance de connexion
 */
public class DBConnection {
    private static DBConnection instance;
    private static Connection connection;
    
    // Paramètres de connexion à la base de données SQLite
    // Utilisation de variables d'environnement pour la production
    // Sur Railway, utilise /tmp pour la persistance ou un chemin de volume
    private static final String DB_PATH = System.getenv("DATABASE_PATH") != null 
        ? System.getenv("DATABASE_PATH") 
        : (System.getenv("RAILWAY_ENVIRONMENT") != null 
            ? "/tmp/blog_jee.db"  // Railway: utilise /tmp pour persistance
            : "blog_jee.db");     // Local: utilise le répertoire courant
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;
    private static final String DB_DRIVER = "org.sqlite.JDBC";
    
    /**
     * Constructeur privé pour empêcher l'instanciation directe
     */
    private DBConnection() {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur : Driver SQLite non trouvé");
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
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
            // Activer les contraintes de clés étrangères pour SQLite
            try (java.sql.Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
        }
        return connection;
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
