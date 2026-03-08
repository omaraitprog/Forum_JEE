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
                
                // Seed initial data if database is empty (works on Railway and local)
                seedInitialData(conn);
            } else {
                logger.info("Base de données déjà initialisée");
                
                // Check if database is empty (no users OR no articles) and seed if needed
                if (isDatabaseEmpty(conn)) {
                    logger.info("Base de données vide détectée, ajout des données initiales...");
                    seedInitialData(conn);
                } else {
                    // Vérifier aussi s'il n'y a pas d'articles (même s'il y a des utilisateurs)
                    String forceSeed = System.getenv("FORCE_SEED");
                    boolean shouldForceSeed = "true".equalsIgnoreCase(forceSeed);
                    
                    try (Statement stmt = conn.createStatement();
                         java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM articles")) {
                        if (rs.next()) {
                            int articleCount = rs.getInt("count");
                            logger.info("Base de données contient " + articleCount + " articles");
                            if (articleCount == 0 || shouldForceSeed) {
                                if (shouldForceSeed) {
                                    logger.info("FORCE_SEED=true détecté, re-seeding de la base de données...");
                                } else {
                                    logger.info("Aucun article trouvé dans la base, ajout des données initiales...");
                                }
                                seedInitialData(conn);
                            }
                        }
                    } catch (SQLException e) {
                        logger.warning("Erreur lors de la vérification des articles: " + e.getMessage());
                    }
                }
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
    
    /**
     * Vérifie si la base de données est vide (aucun utilisateur)
     */
    private boolean isDatabaseEmpty(java.sql.Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM utilisateurs")) {
            if (rs.next()) {
                return rs.getInt("count") == 0;
            }
            return true;
        }
    }
    
    /**
     * Ajoute les données initiales (seed data) à la base de données
     * Utilise les données de seeders.sql pour peupler la base
     * Fonctionne automatiquement sur Railway et en local si la base est vide
     */
    private void seedInitialData(java.sql.Connection conn) throws SQLException {
        // Vérifier si on doit désactiver le seed (optionnel, via variable d'environnement)
        String seedDisabled = System.getenv("DISABLE_SEED");
        if ("true".equalsIgnoreCase(seedDisabled)) {
            logger.info("Seed désactivé via DISABLE_SEED=true");
            return;
        }
        
        // Vérifier si on doit forcer le re-seed (optionnel, via variable d'environnement)
        String forceSeed = System.getenv("FORCE_SEED");
        boolean shouldForceSeed = "true".equalsIgnoreCase(forceSeed);
        
        // Le seed fonctionne automatiquement sur Railway et en local
        // Il ne s'exécute que si la base est vide (vérifié dans initializeDatabase)
        // OU si FORCE_SEED=true est défini
        
        try {
            // Activer les clés étrangères
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
            
            // Désactiver l'auto-commit pour une transaction
            conn.setAutoCommit(false);
            
            logger.info("Ajout des données initiales...");
            
            // Insérer les utilisateurs avec PreparedStatement
            logger.info("Insertion des utilisateurs...");
            String userSql = "INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, role, actif, bio, date_inscription) VALUES (?, ?, ?, ?, ?, ?, ?, datetime('now', ?))";
            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(userSql)) {
                String passwordHash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
                
                // Admin Principal
                pstmt.setString(1, "Admin");
                pstmt.setString(2, "Principal");
                pstmt.setString(3, "admin@forum.com");
                pstmt.setString(4, passwordHash);
                pstmt.setString(5, "ADMIN");
                pstmt.setInt(6, 1);
                pstmt.setString(7, "Administrateur principal du forum. Passionné de technologie et de développement web.");
                pstmt.setString(8, "-30 days");
                pstmt.executeUpdate();
                
                // Jean Dupont
                pstmt.setString(1, "Dupont");
                pstmt.setString(2, "Jean");
                pstmt.setString(3, "jean.dupont@forum.com");
                pstmt.setString(4, passwordHash);
                pstmt.setString(5, "ADMIN");
                pstmt.setInt(6, 1);
                pstmt.setString(7, "Co-administrateur et développeur full-stack avec 10 ans d'expérience.");
                pstmt.setString(8, "-25 days");
                pstmt.executeUpdate();
                
                // Sophie Martin
                pstmt.setString(1, "Martin");
                pstmt.setString(2, "Sophie");
                pstmt.setString(3, "sophie.martin@forum.com");
                pstmt.setString(4, passwordHash);
                pstmt.setString(5, "MEMBRE");
                pstmt.setInt(6, 1);
                pstmt.setString(7, "Développeuse Java passionnée par les architectures d'entreprise et les bonnes pratiques.");
                pstmt.setString(8, "-20 days");
                pstmt.executeUpdate();
                
                // Pierre Bernard
                pstmt.setString(1, "Bernard");
                pstmt.setString(2, "Pierre");
                pstmt.setString(3, "pierre.bernard@forum.com");
                pstmt.setString(4, passwordHash);
                pstmt.setString(5, "MEMBRE");
                pstmt.setInt(6, 1);
                pstmt.setString(7, "Expert en sécurité web et en développement d'applications sécurisées.");
                pstmt.setString(8, "-18 days");
                pstmt.executeUpdate();
                
                // Marie Dubois
                pstmt.setString(1, "Dubois");
                pstmt.setString(2, "Marie");
                pstmt.setString(3, "marie.dubois@forum.com");
                pstmt.setString(4, passwordHash);
                pstmt.setString(5, "MEMBRE");
                pstmt.setInt(6, 1);
                pstmt.setString(7, "Designer UI/UX et développeuse frontend. Amoureuse du design moderne et des interfaces intuitives.");
                pstmt.setString(8, "-15 days");
                pstmt.executeUpdate();
                
                // Thomas Lefebvre
                pstmt.setString(1, "Lefebvre");
                pstmt.setString(2, "Thomas");
                pstmt.setString(3, "thomas.lefebvre@forum.com");
                pstmt.setString(4, passwordHash);
                pstmt.setString(5, "MEMBRE");
                pstmt.setInt(6, 1);
                pstmt.setString(7, "Développeur backend spécialisé en Java EE et microservices.");
                pstmt.setString(8, "-12 days");
                pstmt.executeUpdate();
                
                // Julie Moreau
                pstmt.setString(1, "Moreau");
                pstmt.setString(2, "Julie");
                pstmt.setString(3, "julie.moreau@forum.com");
                pstmt.setString(4, passwordHash);
                pstmt.setString(5, "MEMBRE");
                pstmt.setInt(6, 1);
                pstmt.setString(7, "Étudiante en informatique passionnée par le développement web et les nouvelles technologies.");
                pstmt.setString(8, "-10 days");
                pstmt.executeUpdate();
                
                // Lucas Petit
                pstmt.setString(1, "Petit");
                pstmt.setString(2, "Lucas");
                pstmt.setString(3, "lucas.petit@forum.com");
                pstmt.setString(4, passwordHash);
                pstmt.setString(5, "MEMBRE");
                pstmt.setInt(6, 0);
                pstmt.setString(7, "Nouveau membre de la communauté.");
                pstmt.setString(8, "-5 days");
                pstmt.executeUpdate();
            }
            
            // Insérer les articles avec PreparedStatement
            logger.info("Insertion des articles...");
            String articleSql = "INSERT INTO articles (titre, contenu, resume, auteur_id, statut, date_creation, date_modification) VALUES (?, ?, ?, ?, ?, datetime('now', ?), ?)";
            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(articleSql)) {
                // Article 1: Bienvenue
                pstmt.setString(1, "Bienvenue sur notre Forum");
                pstmt.setString(2, "Bienvenue dans notre communauté de développeurs ! Ce forum est un espace dédié au partage de connaissances, d'expériences et de meilleures pratiques dans le domaine du développement web et des technologies Java.\n\n## Notre Mission\n\nNotre objectif est de créer une communauté active et bienveillante où chacun peut :\n- Partager ses connaissances et expériences\n- Poser des questions et obtenir de l'aide\n- Découvrir les dernières tendances technologiques\n- Collaborer sur des projets passionnants\n\n## Les Règles de la Communauté\n\n1. Respect mutuel entre tous les membres\n2. Partage de contenu de qualité et pertinent\n3. Aide et entraide pour les développeurs de tous niveaux\n4. Respect des droits d'auteur et des licences\n\nN'hésitez pas à explorer les articles, à commenter et à participer activement aux discussions. Ensemble, nous pouvons créer une ressource précieuse pour tous les développeurs !\n\nBonne navigation et à bientôt sur le forum !");
                pstmt.setString(3, "Un message de bienvenue pour tous les nouveaux membres de notre communauté de développeurs.");
                pstmt.setInt(4, 1);
                pstmt.setString(5, "PUBLIE");
                pstmt.setString(6, "-25 days");
                pstmt.setNull(7, java.sql.Types.VARCHAR); // date_modification null
                pstmt.executeUpdate();
                
                // Article 2: Java EE
                pstmt.setString(1, "Introduction à Java Enterprise Edition (Java EE)");
                pstmt.setString(2, "Java Enterprise Edition, maintenant connu sous le nom de Jakarta EE, est une plateforme puissante pour le développement d'applications d'entreprise. Dans cet article, nous explorerons les concepts fondamentaux.\n\n## Qu'est-ce que Java EE ?\n\nJava EE fournit un ensemble d'API et de technologies standardisées pour créer des applications distribuées, robustes et scalables. Il s'appuie sur Java SE et ajoute des fonctionnalités pour les applications d'entreprise.\n\n## Composants Principaux\n\n### 1. Servlets et JSP\nLes Servlets permettent de créer des applications web dynamiques, tandis que JSP (JavaServer Pages) facilite la création de pages web avec du code Java intégré.\n\n### 2. Enterprise JavaBeans (EJB)\nLes EJB fournissent un modèle de composants pour la logique métier, avec support des transactions, de la sécurité et de la concurrence.\n\n### 3. Java Persistence API (JPA)\nJPA simplifie l'accès aux données en fournissant une couche d'abstraction pour la persistance des objets Java.\n\n### 4. JavaServer Faces (JSF)\nJSF est un framework pour créer des interfaces utilisateur web basées sur des composants.\n\n## Avantages de Java EE\n\n- **Standardisation** : Basé sur des standards ouverts\n- **Scalabilité** : Conçu pour les applications d'entreprise\n- **Sécurité** : Mécanismes de sécurité intégrés\n- **Portabilité** : Fonctionne sur différentes plateformes\n\nJava EE reste une excellente choix pour les applications d'entreprise nécessitant robustesse et scalabilité.");
                pstmt.setString(3, "Découvrez les bases de Java EE et comment cette plateforme peut vous aider à développer des applications d'entreprise robustes et scalables.");
                pstmt.setInt(4, 1);
                pstmt.setString(5, "PUBLIE");
                pstmt.setString(6, "-22 days");
                pstmt.setNull(7, java.sql.Types.VARCHAR); // date_modification will be set separately
                pstmt.executeUpdate();
                
                // Article 3: Sécurité Web
                pstmt.setString(1, "Les Meilleures Pratiques de Sécurité Web");
                pstmt.setString(2, "La sécurité web est un aspect crucial du développement d'applications modernes. Voici un guide complet des meilleures pratiques essentielles.\n\n## 1. Authentification et Autorisation\n\n### Authentification Robuste\n- Utilisez des mécanismes d'authentification multi-facteurs (MFA) quand c'est possible\n- Implémentez des politiques de mots de passe fortes\n- Limitez les tentatives de connexion pour prévenir les attaques par force brute\n\n### Contrôle d'Accès\n- Implémentez le principe du moindre privilège\n- Utilisez des rôles et permissions granulaires\n- Validez les autorisations côté serveur, pas seulement côté client\n\n## 2. Protection des Mots de Passe\n\n**Ne stockez JAMAIS les mots de passe en clair !**\n\nUtilisez des algorithmes de hachage sécurisés :\n- **BCrypt** : Recommandé pour la plupart des cas\n- **Argon2** : Pour des besoins de sécurité élevés\n- **PBKDF2** : Alternative fiable\n\n## 3. Protection CSRF (Cross-Site Request Forgery)\n\nImplémentez des tokens CSRF pour protéger vos formulaires :\n- Générez un token unique pour chaque session\n- Incluez le token dans tous les formulaires\n- Validez le token côté serveur à chaque soumission\n\n## 4. Validation et Échappement des Entrées\n\n- **Validation** : Vérifiez le format et le type de toutes les entrées\n- **Échappement** : Échappez les données avant de les afficher\n- **Préparation des requêtes** : Utilisez toujours des requêtes préparées pour prévenir les injections SQL\n\n## 5. HTTPS et Chiffrement\n\n- Utilisez **toujours HTTPS en production**\n- Configurez correctement les certificats SSL/TLS\n- Implémentez HSTS (HTTP Strict Transport Security)\n- Chiffrez les données sensibles au repos\n\nEn suivant ces pratiques, vous pouvez considérablement améliorer la sécurité de vos applications web.");
                pstmt.setString(3, "Apprenez les meilleures pratiques de sécurité essentielles pour protéger vos applications web contre les menaces courantes et modernes.");
                pstmt.setInt(4, 3);
                pstmt.setString(5, "PUBLIE");
                pstmt.setString(6, "-20 days");
                pstmt.setNull(7, java.sql.Types.VARCHAR); // date_modification null
                pstmt.executeUpdate();
                
                // Article 4: MVC
                pstmt.setString(1, "Guide Complet du Pattern MVC");
                pstmt.setString(2, "Le pattern Model-View-Controller (MVC) est l'un des patterns d'architecture les plus utilisés dans le développement web. Comprendre et bien implémenter MVC est essentiel pour créer des applications maintenables.\n\n## Architecture MVC\n\nMVC sépare l'application en trois composants principaux avec des responsabilités distinctes :\n\n### Model (Modèle)\nLe modèle représente les données et la logique métier de l'application.\n\n**Responsabilités :**\n- Gérer l'accès aux données (base de données, fichiers, APIs)\n- Contenir la logique métier et les règles de l'application\n- Notifier la vue des changements d'état\n\n### View (Vue)\nLa vue représente l'interface utilisateur et affiche les données du modèle.\n\n**Responsabilités :**\n- Présenter les données à l'utilisateur\n- Capturer les interactions utilisateur\n- Être passive (ne contient pas de logique métier)\n\n### Controller (Contrôleur)\nLe contrôleur gère les interactions entre le modèle et la vue.\n\n**Responsabilités :**\n- Recevoir les requêtes utilisateur\n- Appeler la logique métier appropriée\n- Sélectionner la vue à afficher\n- Gérer la navigation\n\n## Avantages du Pattern MVC\n\n1. **Séparation des préoccupations** : Chaque composant a une responsabilité claire\n2. **Maintenabilité** : Facilite la maintenance et les modifications\n3. **Testabilité** : Permet de tester chaque composant indépendamment\n4. **Réutilisabilité** : Les modèles et vues peuvent être réutilisés\n5. **Évolutivité** : Facilite l'ajout de nouvelles fonctionnalités\n\nCette séparation des responsabilités facilite la maintenance, les tests et l'évolution de l'application.");
                pstmt.setString(3, "Comprenez le pattern MVC en profondeur et apprenez comment l'implémenter efficacement dans vos projets web pour créer des applications bien structurées.");
                pstmt.setInt(4, 3);
                pstmt.setString(5, "PUBLIE");
                pstmt.setString(6, "-18 days");
                pstmt.setNull(7, java.sql.Types.VARCHAR); // date_modification null
                pstmt.executeUpdate();
                
                // Article 5: SQLite
                pstmt.setString(1, "SQLite : Guide Complet pour le Développement Web");
                pstmt.setString(2, "SQLite est l'un des systèmes de gestion de bases de données relationnelles les plus populaires au monde. Il est particulièrement adapté au développement web pour de nombreuses raisons.\n\n## Qu'est-ce que SQLite ?\n\nSQLite est un moteur de base de données SQL embarqué, léger et sans serveur. Contrairement à MySQL ou PostgreSQL, SQLite ne nécessite pas de processus serveur séparé.\n\n## Caractéristiques Principales\n\n### Avantages\n- **Léger** : Bibliothèque de quelques centaines de KB\n- **Sans configuration** : Pas besoin de serveur ou de configuration complexe\n- **Rapide** : Excellentes performances pour la plupart des applications\n- **ACID** : Support complet des transactions ACID\n- **Portable** : Un seul fichier de base de données\n- **Open Source** : Licence publique du domaine\n\n### Limitations\n- Pas de gestion des utilisateurs (pas de système d'authentification)\n- Accès concurrent en écriture limité\n- Taille maximale recommandée : quelques GB\n- Pas idéal pour les applications haute concurrence\n\n## Utilisation dans le Développement Web\n\n### Quand Utiliser SQLite\n\n✅ **Idéal pour :**\n- Prototypes et applications de petite à moyenne taille\n- Applications avec peu d'accès concurrents en écriture\n- Développement et tests\n- Applications embarquées\n- Sites web avec trafic modéré\n\n❌ **À éviter pour :**\n- Applications nécessitant beaucoup d'accès concurrents en écriture\n- Applications très volumineuses (plusieurs GB)\n- Systèmes nécessitant des utilisateurs multiples avec permissions\n\nSQLite est un excellent choix pour démarrer rapidement et évoluer progressivement.");
                pstmt.setString(3, "Découvrez comment SQLite peut être utilisé efficacement dans vos projets web, ses avantages, limitations et meilleures pratiques.");
                pstmt.setInt(4, 1);
                pstmt.setString(5, "PUBLIE");
                pstmt.setString(6, "-15 days");
                pstmt.setNull(7, java.sql.Types.VARCHAR); // date_modification null
                pstmt.executeUpdate();
                
                // Article 6: Servlets
                pstmt.setString(1, "Introduction aux Servlets Java");
                pstmt.setString(2, "Les Servlets sont la base du développement web en Java. Dans cet article, nous explorerons les concepts fondamentaux des Servlets.\n\n## Qu'est-ce qu'une Servlet ?\n\nUne Servlet est une classe Java qui étend les fonctionnalités d'un serveur web. Elle traite les requêtes HTTP et génère des réponses dynamiques.\n\n## Cycle de Vie d'une Servlet\n\n1. **Chargement et instanciation** : Le conteneur charge la classe\n2. **Initialisation** : `init()` est appelée une fois\n3. **Traitement des requêtes** : `service()`, `doGet()`, `doPost()` sont appelées\n4. **Destruction** : `destroy()` est appelée lors de l'arrêt\n\nLes Servlets sont essentielles pour tout développeur Java web !");
                pstmt.setString(3, "Apprenez les bases des Servlets Java et comment créer des applications web dynamiques avec cette technologie fondamentale.");
                pstmt.setInt(4, 4);
                pstmt.setString(5, "PUBLIE");
                pstmt.setString(6, "-12 days");
                pstmt.setNull(7, java.sql.Types.VARCHAR); // date_modification null
                pstmt.executeUpdate();
                
                // Article 7: JSP
                pstmt.setString(1, "Les Fondamentaux de JSP (JavaServer Pages)");
                pstmt.setString(2, "JSP permet de créer des pages web dynamiques en mélangeant HTML et Java. C'est une technologie complémentaire aux Servlets.\n\n## Syntaxe JSP\n\n### Scriptlets\n```jsp\n<%\n    String name = request.getParameter(\"name\");\n    out.println(\"Hello \" + name);\n%>\n```\n\n### Expressions\n```jsp\n<p>Bienvenue <%= user.getName() %></p>\n```\n\n### Directives\n```jsp\n<%@ page import=\"java.util.List\" %>\n<%@ include file=\"header.jsp\" %>\n```\n\n## JSTL (JSP Standard Tag Library)\n\nJSTL simplifie le code JSP :\n```jsp\n<c:forEach var=\"article\" items=\"${articles}\">\n    <h2>${article.title}</h2>\n    <p>${article.content}</p>\n</c:forEach>\n```\n\nJSP facilite la création d'interfaces utilisateur dynamiques !");
                pstmt.setString(3, "Découvrez comment utiliser JSP pour créer des pages web dynamiques et interactives avec Java.");
                pstmt.setInt(4, 4);
                pstmt.setString(5, "PUBLIE");
                pstmt.setString(6, "-10 days");
                pstmt.setNull(7, java.sql.Types.VARCHAR); // date_modification null
                pstmt.executeUpdate();
                
                // Article 8: Déploiement
                pstmt.setString(1, "Guide de Déploiement d'Applications Java Web");
                pstmt.setString(2, "Le déploiement d'applications Java web peut sembler complexe, mais avec les bonnes pratiques, c'est tout à fait gérable.\n\n## Formats de Déploiement\n\n### WAR (Web Application Archive)\nUn fichier WAR contient toute votre application web :\n- Structure standardisée\n- Facile à déployer sur Tomcat, Jetty, etc.\n- Peut être créé avec Maven ou Gradle\n\n## Serveurs d'Applications\n\n### Apache Tomcat\n- Léger et rapide\n- Idéal pour les Servlets et JSP\n- Configuration simple\n\n### Jetty\n- Très léger\n- Excellent pour le développement\n- Support embarqué\n\n## Variables d'Environnement\n\nUtilisez des variables d'environnement pour la configuration :\n- Chemins de base de données\n- Clés API\n- Paramètres de connexion\n\n## Bonnes Pratiques\n\n1. Externalisez la configuration\n2. Utilisez des profils (dev, prod)\n3. Configurez correctement les logs\n4. Testez en environnement de staging\n\nLe déploiement devient simple avec de la pratique !");
                pstmt.setString(3, "Apprenez les meilleures pratiques pour déployer vos applications Java web en production de manière efficace et sécurisée.");
                pstmt.setInt(4, 2);
                pstmt.setString(5, "PUBLIE");
                pstmt.setString(6, "-8 days");
                pstmt.setNull(7, java.sql.Types.VARCHAR); // date_modification null
                pstmt.executeUpdate();
            }
            
            // Mettre à jour date_modification pour l'article 2 (Java EE)
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("UPDATE articles SET date_modification = datetime('now', '-20 days') WHERE id = 2");
            }
            
            // Insérer les commentaires avec PreparedStatement
            logger.info("Insertion des commentaires...");
            String commentSql = "INSERT INTO commentaires (contenu, article_id, auteur_id, date_creation, approuve) VALUES (?, ?, ?, datetime('now', ?), ?)";
            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(commentSql)) {
                // Commentaires pour article 2
                pstmt.setString(1, "Excellent article ! Merci pour ce partage. C'est exactement ce dont j'avais besoin pour comprendre Java EE.");
                pstmt.setInt(2, 2);
                pstmt.setInt(3, 3);
                pstmt.setString(4, "-21 days");
                pstmt.setInt(5, 1);
                pstmt.executeUpdate();
                
                pstmt.setString(1, "Très intéressant, j'ai appris beaucoup de choses. Pourriez-vous faire un article plus détaillé sur JPA ?");
                pstmt.setInt(2, 2);
                pstmt.setInt(3, 4);
                pstmt.setString(4, "-20 days");
                pstmt.setInt(5, 1);
                pstmt.executeUpdate();
                
                // Commentaires pour article 3
                pstmt.setString(1, "La sécurité est effectivement cruciale. Merci pour ces conseils pratiques, je vais les appliquer dans mon projet.");
                pstmt.setInt(2, 3);
                pstmt.setInt(3, 1);
                pstmt.setString(4, "-19 days");
                pstmt.setInt(5, 1);
                pstmt.executeUpdate();
                
                pstmt.setString(1, "Super article sur la sécurité ! Pourriez-vous approfondir la partie sur les tokens CSRF ?");
                pstmt.setInt(2, 3);
                pstmt.setInt(3, 2);
                pstmt.setString(4, "-18 days");
                pstmt.setInt(5, 1);
                pstmt.executeUpdate();
                
                // Commentaires pour article 4
                pstmt.setString(1, "Le pattern MVC est vraiment essentiel. Cet article explique très bien les concepts. Merci !");
                pstmt.setInt(2, 4);
                pstmt.setInt(3, 5);
                pstmt.setString(4, "-17 days");
                pstmt.setInt(5, 1);
                pstmt.executeUpdate();
                
                pstmt.setString(1, "J'ai une question : comment gérer les dépendances circulaires entre le modèle et le contrôleur ?");
                pstmt.setInt(2, 4);
                pstmt.setInt(3, 6);
                pstmt.setString(4, "-16 days");
                pstmt.setInt(5, 1);
                pstmt.executeUpdate();
                
                // Commentaires pour article 5
                pstmt.setString(1, "SQLite est effectivement un excellent choix pour démarrer. Merci pour ce guide complet !");
                pstmt.setInt(2, 5);
                pstmt.setInt(3, 3);
                pstmt.setString(4, "-14 days");
                pstmt.setInt(5, 1);
                pstmt.executeUpdate();
                
                pstmt.setString(1, "Très bon article ! J'utilise SQLite dans mon projet et je confirme que c'est parfait pour les petites applications.");
                pstmt.setInt(2, 5);
                pstmt.setInt(3, 7);
                pstmt.setString(4, "-13 days");
                pstmt.setInt(5, 1);
                pstmt.executeUpdate();
                
                // Commentaires pour article 6
                pstmt.setString(1, "Les Servlets sont la base de tout développement web Java. Merci pour cette introduction claire.");
                pstmt.setInt(2, 6);
                pstmt.setInt(3, 4);
                pstmt.setString(4, "-11 days");
                pstmt.setInt(5, 1);
                pstmt.executeUpdate();
                
                // Commentaires pour article 7
                pstmt.setString(1, "JSP est très pratique pour créer des interfaces. Avez-vous des exemples avec JSTL ?");
                pstmt.setInt(2, 7);
                pstmt.setInt(3, 5);
                pstmt.setString(4, "-9 days");
                pstmt.setInt(5, 1);
                pstmt.executeUpdate();
                
                // Commentaires pour article 8
                pstmt.setString(1, "Excellent guide de déploiement ! Les variables d'environnement sont effectivement essentielles.");
                pstmt.setInt(2, 8);
                pstmt.setInt(3, 6);
                pstmt.setString(4, "-7 days");
                pstmt.setInt(5, 1);
                pstmt.executeUpdate();
                
                pstmt.setString(1, "Merci pour ces informations. Pourriez-vous faire un article sur le déploiement sur Railway ou Heroku ?");
                pstmt.setInt(2, 8);
                pstmt.setInt(3, 7);
                pstmt.setString(4, "-6 days");
                pstmt.setInt(5, 1);
                pstmt.executeUpdate();
                
                // Commentaires pour article 1
                pstmt.setString(1, "Bienvenue à tous les nouveaux membres ! N'hésitez pas à poser des questions.");
                pstmt.setInt(2, 1);
                pstmt.setInt(3, 2);
                pstmt.setString(4, "-24 days");
                pstmt.setInt(5, 1);
                pstmt.executeUpdate();
                
                pstmt.setString(1, "Super communauté ! Je suis ravi de faire partie de ce forum.");
                pstmt.setInt(2, 1);
                pstmt.setInt(3, 3);
                pstmt.setString(4, "-23 days");
                pstmt.setInt(5, 1);
                pstmt.executeUpdate();
            }
            
            // Commit la transaction
            conn.commit();
            conn.setAutoCommit(true);
            
            // Vérifier que les données ont été insérées
            try (Statement stmt = conn.createStatement()) {
                // Vérifier les utilisateurs
                try (java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM utilisateurs")) {
                    if (rs.next()) {
                        int userCount = rs.getInt("count");
                        logger.info("Nombre d'utilisateurs dans la base: " + userCount);
                    }
                }
                
                // Vérifier les articles
                try (java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM articles")) {
                    if (rs.next()) {
                        int articleCount = rs.getInt("count");
                        logger.info("Nombre d'articles dans la base: " + articleCount);
                    }
                }
                
                // Vérifier un utilisateur spécifique
                try (java.sql.ResultSet rs = stmt.executeQuery(
                        "SELECT email, actif, role FROM utilisateurs WHERE email = 'admin@forum.com'")) {
                    if (rs.next()) {
                        logger.info("Utilisateur admin@forum.com trouvé - Actif: " + rs.getInt("actif") + ", Role: " + rs.getString("role"));
                    } else {
                        logger.warning("Utilisateur admin@forum.com NON TROUVÉ dans la base de données !");
                    }
                }
            }
            
            logger.info("Données initiales ajoutées avec succès !");
            logger.info("Vous pouvez vous connecter avec:");
            logger.info("  - Email: admin@forum.com");
            logger.info("  - Mot de passe: password123");
            logger.info("  - Email: jean.dupont@forum.com");
            logger.info("  - Mot de passe: password123");
        } catch (SQLException e) {
            // Rollback en cas d'erreur
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }
            } catch (SQLException rollbackEx) {
                logger.severe("Erreur lors du rollback: " + rollbackEx.getMessage());
                rollbackEx.printStackTrace();
            }
            logger.severe("Erreur lors de l'ajout des données initiales: " + e.getMessage());
            logger.severe("Code d'erreur SQL: " + e.getErrorCode());
            logger.severe("État SQL: " + e.getSQLState());
            // Ne pas faire échouer l'application si le seed échoue, mais logger l'erreur
            e.printStackTrace();
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
