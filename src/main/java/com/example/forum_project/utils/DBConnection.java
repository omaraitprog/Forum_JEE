package com.example.forum_project.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitaire pour la gestion de la connexion à la base de données MySQL
 * Implémente le pattern Singleton pour une seule instance de connexion
 * Supporte les variables d'environnement Railway (MYSQLHOST, etc.) et les variables personnalisées (DB_HOST, etc.)
 */
public class DBConnection {
    private static DBConnection instance;
    private static Connection connection;
    
    // Nombre max de tentatives de connexion (utile quand MySQL démarre en même temps que l'app)
    private static final int MAX_RETRIES = 5;
    private static final int RETRY_DELAY_MS = 3000; // 3 secondes entre chaque tentative
    
    // Paramètres de connexion à la base de données MySQL
    // Priorité : MYSQL_URL/DATABASE_URL (URL complète) > DB_* > MYSQL* (Railway) > valeurs par défaut
    private static final String DB_HOST = resolveEnv(new String[]{"DB_HOST", "MYSQLHOST", "MYSQL_HOST"}, "localhost");
    private static final String DB_PORT = resolveEnv(new String[]{"DB_PORT", "MYSQLPORT", "MYSQL_PORT"}, "3306");
    private static final String DB_NAME = resolveEnv(new String[]{"DB_NAME", "MYSQLDATABASE", "MYSQL_DATABASE"}, "blog_jee");
    private static final String DB_USER = resolveEnv(new String[]{"DB_USER", "MYSQLUSER", "MYSQL_USER"}, "root");
    private static final String DB_PASSWORD = resolveEnv(new String[]{"DB_PASSWORD", "MYSQLPASSWORD", "MYSQL_PASSWORD"}, "");
    
    private static final String DB_URL = buildJdbcUrl();
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    static {
        System.out.println("=== DBConnection Initialization ===");
        System.out.println("DB_HOST resolved to: " + DB_HOST);
        System.out.println("DB_PORT resolved to: " + DB_PORT);
        System.out.println("DB_NAME resolved to: " + DB_NAME);
        System.out.println("DB_USER resolved to: " + DB_USER);
        System.out.println("URL de la base de données: " + DB_URL);
    }
    
    /**
     * Construit l'URL JDBC. Si MYSQL_URL ou DATABASE_URL est fourni (par Railway),
     * l'utilise directement. Sinon, construit l'URL à partir des composants individuels.
     */
    private static String buildJdbcUrl() {
        // Vérifier si une URL JDBC complète est fournie (Railway ou config manuelle)
        String fullUrl = resolveEnv(new String[]{"DATABASE_URL", "MYSQL_URL", "JDBC_DATABASE_URL"}, null);
        if (fullUrl != null && !fullUrl.isEmpty()) {
            // Si l'URL commence par mysql:// (format Railway), la convertir en jdbc:mysql://
            if (fullUrl.startsWith("mysql://")) {
                fullUrl = parseMysqlProtocolUrl(fullUrl);
            }
            // Si c'est déjà une URL JDBC, l'utiliser telle quelle (avec paramètres supplémentaires si absents)
            if (fullUrl.startsWith("jdbc:mysql://")) {
                if (!fullUrl.contains("useSSL")) {
                    String separator = fullUrl.contains("?") ? "&" : "?";
                    fullUrl += separator + "useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
                }
                System.out.println("Utilisation de l'URL JDBC fournie par l'environnement");
                return fullUrl;
            }
        }
        
        // Construire l'URL à partir des composants individuels
        return "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
                + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
    }
    
    /**
     * Convertit une URL au format mysql://user:password@host:port/database en JDBC URL.
     * Railway fournit parfois MYSQL_URL dans ce format.
     */
    private static String parseMysqlProtocolUrl(String url) {
        try {
            // Format: mysql://user:password@host:port/database
            String withoutProtocol = url.substring("mysql://".length());
            String hostPart;
            
            if (withoutProtocol.contains("@")) {
                // Skip user:password part, we use DB_USER and DB_PASSWORD separately
                hostPart = withoutProtocol.substring(withoutProtocol.indexOf("@") + 1);
            } else {
                hostPart = withoutProtocol;
            }
            
            String jdbcUrl = "jdbc:mysql://" + hostPart;
            System.out.println("Converted mysql:// URL to JDBC: " + jdbcUrl);
            return jdbcUrl;
        } catch (Exception e) {
            System.err.println("Erreur lors du parsing de l'URL MySQL: " + e.getMessage());
            return url;
        }
    }
    
    /**
     * Résout une variable d'environnement en essayant plusieurs noms possibles.
     * Retourne la première variable trouvée, ou la valeur par défaut.
     */
    private static String resolveEnv(String[] keys, String defaultValue) {
        for (String key : keys) {
            String value = System.getenv(key);
            if (value != null && !value.isEmpty()) {
                System.out.println("  [env] " + key + " = " + (key.contains("PASSWORD") ? "****" : value));
                return value;
            }
        }
        return defaultValue;
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
     * Obtient une connexion à la base de données avec retry automatique.
     * Retente la connexion plusieurs fois si MySQL n'est pas encore prêt
     * (race condition classique au démarrage de Docker / Railway).
     * @return Connection à la base de données
     * @throws SQLException si une erreur survient après toutes les tentatives
     */
    public Connection getConnection() throws SQLException {
        // Vérifier si la connexion existante est toujours valide
        try {
            if (connection != null && !connection.isClosed()) {
                try {
                    if (connection.isValid(2)) {
                        return connection;
                    }
                } catch (SQLException e) {
                    System.err.println("La connexion n'est plus valide, création d'une nouvelle connexion");
                    connection = null;
                }
            }
        } catch (SQLException e) {
            connection = null;
        }
        
        // Créer une nouvelle connexion avec retry
        SQLException lastException = null;
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                System.out.println("Tentative de connexion " + attempt + "/" + MAX_RETRIES + " à: " + DB_URL);
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Connexion établie avec succès (tentative " + attempt + ")");
                return connection;
            } catch (SQLException e) {
                lastException = e;
                System.err.println("Tentative " + attempt + "/" + MAX_RETRIES + " échouée: " + e.getMessage()
                        + " [SQLState: " + e.getSQLState() + ", ErrorCode: " + e.getErrorCode() + "]");
                connection = null;
                
                // Si c'est un problème de connexion (08S01) et qu'on a encore des tentatives, attendre et réessayer
                if (attempt < MAX_RETRIES && ("08S01".equals(e.getSQLState()) || "08001".equals(e.getSQLState())
                        || e.getMessage().contains("Communications link failure")
                        || e.getMessage().contains("Connection refused"))) {
                    System.out.println("MySQL n'est peut-être pas encore prêt. Attente de " + RETRY_DELAY_MS + "ms avant la prochaine tentative...");
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw e;
                    }
                } else if (attempt >= MAX_RETRIES) {
                    // Dernière tentative échouée, afficher un diagnostic complet
                    System.err.println("=== ÉCHEC DE CONNEXION APRÈS " + MAX_RETRIES + " TENTATIVES ===");
                    System.err.println("URL: " + DB_URL);
                    System.err.println("User: " + DB_USER);
                    System.err.println("Host: " + DB_HOST);
                    System.err.println("Port: " + DB_PORT);
                    System.err.println("Database: " + DB_NAME);
                    System.err.println("Vérifiez que MySQL est démarré et accessible à cette adresse.");
                    System.err.println("Si Railway: assurez-vous que MYSQLHOST, MYSQLPORT, etc. sont définis.");
                    System.err.println("===============================================");
                } else {
                    // Erreur non liée à la connectivité (mauvais mot de passe, etc.), ne pas réessayer
                    break;
                }
            }
        }
        
        throw lastException;
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
