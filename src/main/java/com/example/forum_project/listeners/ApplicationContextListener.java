package com.example.forum_project.listeners;

import com.example.forum_project.utils.DBConnection;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Logger;

/**
 * Listener pour nettoyer les ressources lors de l'arrêt de l'application
 * Résout les problèmes de fuite de mémoire liés au pilote JDBC MySQL
 */
@WebListener
public class ApplicationContextListener implements ServletContextListener {
    
    private static final Logger logger = Logger.getLogger(ApplicationContextListener.class.getName());
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Application démarrée");
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
        
        // 2. Arrêter le thread de nettoyage des connexions abandonnées MySQL
        try {
            Class<?> clazz = Class.forName("com.mysql.cj.jdbc.AbandonedConnectionCleanupThread");
            java.lang.reflect.Method method = clazz.getMethod("checkedShutdown");
            method.invoke(null);
            logger.info("Thread de nettoyage MySQL arrêté");
        } catch (Exception e) {
            // Ignorer si la classe n'existe pas ou si la méthode n'est pas disponible
            logger.fine("Impossible d'arrêter le thread de nettoyage MySQL (peut être normal): " + e.getMessage());
        }
        
        // 3. Désenregistrer tous les pilotes JDBC
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
