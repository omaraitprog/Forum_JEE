package com.example.forum_project.listeners;

import com.example.forum_project.utils.DBConnection;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.logging.Logger;

/**
 * Listener pour nettoyer les ressources lors de l'arrêt de l'application
 * Nettoie les ressources JDBC lors de l'arrêt de l'application
 * Initialise la base de données SQLite si les tables n'existent pas
 */
@WebListener
public class ApplicationContextListener implements ServletContextListener {
    
    private static final Logger logger = Logger.getLogger(ApplicationContextListener.class.getName());
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Application démarrée");
        
        // Initialiser la base de données SQLite si nécessaire
        try {
            initializeDatabase();
        } catch (Exception e) {
            logger.severe("Erreur lors de l'initialisation de la base de données: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Initialise la base de données en créant les tables si elles n'existent pas
     */
    private void initializeDatabase() throws SQLException {
        try (java.sql.Connection conn = DBConnection.getInstance().getConnection()) {
            // Log le chemin de la base de données pour le débogage
            String dbUrl = conn.getMetaData().getURL();
            logger.info("Connexion à la base de données: " + dbUrl);
            
            // Vérifier si la table utilisateurs existe
            boolean tableExists = false;
            try (Statement stmt = conn.createStatement();
                 java.sql.ResultSet rs = stmt.executeQuery(
                     "SELECT name FROM sqlite_master WHERE type='table' AND name='utilisateurs'")) {
                tableExists = rs.next();
            }
            
            if (!tableExists) {
                logger.info("Initialisation de la base de données SQLite...");
                createTables(conn);
                logger.info("Base de données initialisée avec succès");
            } else {
                logger.info("Base de données déjà initialisée");
            }
        } catch (SQLException e) {
            logger.severe("Erreur SQL lors de l'initialisation: " + e.getMessage());
            logger.severe("Code d'erreur SQL: " + e.getErrorCode());
            logger.severe("État SQL: " + e.getSQLState());
            throw e;
        }
    }
    
    /**
     * Crée les tables de la base de données
     */
    private void createTables(java.sql.Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Activer les clés étrangères
            stmt.execute("PRAGMA foreign_keys = ON");
            
            logger.info("Création de la table utilisateurs...");
            // Créer la table utilisateurs
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS utilisateurs (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nom VARCHAR(100) NOT NULL,
                    prenom VARCHAR(100) NOT NULL,
                    email VARCHAR(150) UNIQUE NOT NULL,
                    mot_de_passe VARCHAR(255) NOT NULL,
                    role TEXT DEFAULT 'MEMBRE' CHECK(role IN ('MEMBRE','ADMIN')),
                    actif INTEGER DEFAULT 0,
                    token_verification VARCHAR(255),
                    date_inscription DATETIME DEFAULT CURRENT_TIMESTAMP,
                    photo_profil VARCHAR(255),
                    bio TEXT
                )
                """);
            
            // Vérifier que la table utilisateurs a été créée
            try (java.sql.ResultSet rs = stmt.executeQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name='utilisateurs'")) {
                if (!rs.next()) {
                    throw new SQLException("La table utilisateurs n'a pas été créée correctement");
                }
                logger.info("Table utilisateurs créée avec succès");
            }
            
            logger.info("Création des index pour utilisateurs...");
            // Créer les index pour utilisateurs
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_email ON utilisateurs(email)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_token ON utilisateurs(token_verification)");
            
            logger.info("Création de la table articles...");
            // Créer la table articles
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS articles (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    titre VARCHAR(255) NOT NULL,
                    contenu TEXT NOT NULL,
                    resume VARCHAR(500),
                    auteur_id INTEGER NOT NULL,
                    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
                    date_modification DATETIME NULL,
                    statut TEXT DEFAULT 'PUBLIE' CHECK(statut IN ('BROUILLON','PUBLIE','ARCHIVE')),
                    image_url VARCHAR(255),
                    FOREIGN KEY (auteur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE
                )
                """);
            
            logger.info("Création des index pour articles...");
            // Créer les index pour articles
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_auteur ON articles(auteur_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_statut ON articles(statut)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_date_creation ON articles(date_creation)");
            
            logger.info("Création de la table commentaires...");
            // Créer la table commentaires
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS commentaires (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    contenu TEXT NOT NULL,
                    article_id INTEGER NOT NULL,
                    auteur_id INTEGER NOT NULL,
                    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
                    approuve INTEGER DEFAULT 1,
                    FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE,
                    FOREIGN KEY (auteur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE
                )
                """);
            
            logger.info("Création des index pour commentaires...");
            // Créer les index pour commentaires
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_article ON commentaires(article_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_auteur_comment ON commentaires(auteur_id)");
            
            logger.info("Toutes les tables ont été créées avec succès");
        }
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Nettoyage des ressources de l'application...");
        
        // 1. Fermer la connexion à la base de données
        try {
            DBConnection dbConnection = DBConnection.getInstance();
            dbConnection.closeConnection();
            logger.info("Connexion à la base de données fermée");
        } catch (Exception e) {
            logger.warning("Erreur lors de la fermeture de la connexion à la base de données: " + e.getMessage());
        }
        
        // 2. Désenregistrer tous les pilotes JDBC
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                logger.info("Pilote JDBC désenregistré: " + driver.getClass().getName());
            } catch (SQLException e) {
                logger.warning("Erreur lors du désenregistrement du pilote " + driver.getClass().getName() + ": " + e.getMessage());
            }
        }
        
        logger.info("Nettoyage des ressources terminé");
    }
}
