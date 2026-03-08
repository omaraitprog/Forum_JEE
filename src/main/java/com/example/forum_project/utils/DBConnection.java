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
    private static final String DB_PATH = getDatabasePath();
    
    static {
        // Log le chemin de la base de données au chargement de la classe
        System.out.println("=== DBConnection Initialization ===");
        System.out.println("Chemin de la base de données: " + DB_PATH);
        System.out.println("URL de la base de données: jdbc:sqlite:" + DB_PATH);
        System.out.println("RAILWAY_ENVIRONMENT: " + System.getenv("RAILWAY_ENVIRONMENT"));
        System.out.println("RAILWAY: " + System.getenv("RAILWAY"));
        System.out.println("DATABASE_PATH: " + System.getenv("DATABASE_PATH"));
        verifyDatabasePath();
    }
    
    /**
     * Détermine le chemin de la base de données en fonction de l'environnement
     * Priorité: DATABASE_PATH > Railway volume (/data) > /tmp > local
     */
    private static String getDatabasePath() {
        // 1. Utiliser DATABASE_PATH si défini explicitement
        String envPath = System.getenv("DATABASE_PATH");
        if (envPath != null && !envPath.isEmpty()) {
            ensureDirectoryExists(envPath);
            return envPath;
        }
        
        // 2. Détecter Railway et essayer les chemins de volume courants
        if (System.getenv("RAILWAY_ENVIRONMENT") != null || 
            System.getenv("RAILWAY") != null ||
            System.getProperty("railway.environment") != null) {
            // Essayer /data (chemin de volume Railway standard)
            java.io.File dataDir = new java.io.File("/data");
            if (dataDir.exists() && dataDir.isDirectory() && dataDir.canWrite()) {
                return "/data/blog_jee.db";
            }
            // Essayer /var/data (autre chemin de volume Railway)
            java.io.File varDataDir = new java.io.File("/var/data");
            if (varDataDir.exists() && varDataDir.isDirectory() && varDataDir.canWrite()) {
                return "/var/data/blog_jee.db";
            }
            // Fallback sur /tmp pour Railway (persiste entre redéploiements)
            ensureDirectoryExists("/tmp/blog_jee.db");
            return "/tmp/blog_jee.db";
        }
        
        // 3. Local: utiliser le répertoire courant
        return "blog_jee.db";
    }
    
    /**
     * S'assure que le répertoire parent du fichier de base de données existe
     * @param dbPath Chemin complet du fichier de base de données
     */
    private static void ensureDirectoryExists(String dbPath) {
        try {
            java.io.File dbFile = new java.io.File(dbPath);
            java.io.File parentDir = dbFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                boolean created = parentDir.mkdirs();
                if (created) {
                    System.out.println("Répertoire créé: " + parentDir.getAbsolutePath());
                } else {
                    System.err.println("Impossible de créer le répertoire: " + parentDir.getAbsolutePath());
                }
            }
            // Vérifier que le répertoire parent est accessible en écriture
            if (parentDir != null && parentDir.exists()) {
                if (!parentDir.canWrite()) {
                    System.err.println("ATTENTION: Le répertoire n'est pas accessible en écriture: " + parentDir.getAbsolutePath());
                } else {
                    System.out.println("Répertoire vérifié et accessible en écriture: " + parentDir.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification/création du répertoire pour: " + dbPath);
            e.printStackTrace();
        }
    }
    
    /**
     * Vérifie que le chemin de la base de données est valide et accessible
     */
    private static void verifyDatabasePath() {
        try {
            java.io.File dbFile = new java.io.File(DB_PATH);
            java.io.File parentDir = dbFile.getParentFile();
            
            if (parentDir == null) {
                // Pas de répertoire parent (fichier dans le répertoire courant)
                System.out.println("Base de données dans le répertoire courant");
            } else {
                System.out.println("Répertoire parent: " + parentDir.getAbsolutePath());
                System.out.println("Répertoire parent existe: " + parentDir.exists());
                System.out.println("Répertoire parent est un répertoire: " + (parentDir.exists() ? parentDir.isDirectory() : "N/A"));
                System.out.println("Répertoire parent est accessible en écriture: " + (parentDir.exists() ? parentDir.canWrite() : "N/A"));
            }
            
            System.out.println("Fichier de base de données existe: " + dbFile.exists());
            if (dbFile.exists()) {
                System.out.println("Fichier de base de données est accessible en écriture: " + dbFile.canWrite());
                System.out.println("Taille du fichier: " + dbFile.length() + " bytes");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification du chemin de la base de données: " + e.getMessage());
            e.printStackTrace();
        }
    }
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
            System.out.println("Chemin de la base de données: " + DB_PATH);
            
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Connexion établie avec succès");
            
            // Activer les contraintes de clés étrangères pour SQLite
            try (java.sql.Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
            
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
