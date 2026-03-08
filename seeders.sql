-- ============================================
-- SEEDERS - STATIC DATA FOR SQLITE DATABASE
-- ============================================
-- This file contains static seed data for the Forum/Blog application
-- Execute this script after creating the database schema
-- 
-- IMPORTANT: Enable foreign keys before running
-- PRAGMA foreign_keys = ON;
-- ============================================

-- ============================================
-- CLEAR EXISTING DATA (Optional - Uncomment if needed)
-- ============================================
-- DELETE FROM commentaires;
-- DELETE FROM articles;
-- DELETE FROM utilisateurs;

-- ============================================
-- INSERT USERS
-- ============================================
-- Note: All passwords are hashed with BCrypt
-- Default password for all users: "password123"
-- Hash: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, role, actif, bio, date_inscription) VALUES
('Admin', 'Principal', 'admin@forum.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', 1, 'Administrateur principal du forum. Passionné de technologie et de développement web.', datetime('now', '-30 days')),
('Dupont', 'Jean', 'jean.dupont@forum.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', 1, 'Co-administrateur et développeur full-stack avec 10 ans d''expérience.', datetime('now', '-25 days')),
('Martin', 'Sophie', 'sophie.martin@forum.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'MEMBRE', 1, 'Développeuse Java passionnée par les architectures d''entreprise et les bonnes pratiques.', datetime('now', '-20 days')),
('Bernard', 'Pierre', 'pierre.bernard@forum.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'MEMBRE', 1, 'Expert en sécurité web et en développement d''applications sécurisées.', datetime('now', '-18 days')),
('Dubois', 'Marie', 'marie.dubois@forum.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'MEMBRE', 1, 'Designer UI/UX et développeuse frontend. Amoureuse du design moderne et des interfaces intuitives.', datetime('now', '-15 days')),
('Lefebvre', 'Thomas', 'thomas.lefebvre@forum.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'MEMBRE', 1, 'Développeur backend spécialisé en Java EE et microservices.', datetime('now', '-12 days')),
('Moreau', 'Julie', 'julie.moreau@forum.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'MEMBRE', 1, 'Étudiante en informatique passionnée par le développement web et les nouvelles technologies.', datetime('now', '-10 days')),
('Petit', 'Lucas', 'lucas.petit@forum.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'MEMBRE', 0, 'Nouveau membre de la communauté.', datetime('now', '-5 days'));

-- ============================================
-- INSERT ARTICLES
-- ============================================

INSERT INTO articles (titre, contenu, resume, auteur_id, statut, date_creation, date_modification) VALUES
('Bienvenue sur notre Forum', 
'Bienvenue dans notre communauté de développeurs ! Ce forum est un espace dédié au partage de connaissances, d''expériences et de meilleures pratiques dans le domaine du développement web et des technologies Java.

## Notre Mission

Notre objectif est de créer une communauté active et bienveillante où chacun peut :
- Partager ses connaissances et expériences
- Poser des questions et obtenir de l''aide
- Découvrir les dernières tendances technologiques
- Collaborer sur des projets passionnants

## Les Règles de la Communauté

1. Respect mutuel entre tous les membres
2. Partage de contenu de qualité et pertinent
3. Aide et entraide pour les développeurs de tous niveaux
4. Respect des droits d''auteur et des licences

N''hésitez pas à explorer les articles, à commenter et à participer activement aux discussions. Ensemble, nous pouvons créer une ressource précieuse pour tous les développeurs !

Bonne navigation et à bientôt sur le forum !',
'Un message de bienvenue pour tous les nouveaux membres de notre communauté de développeurs.',
1, 'PUBLIE', datetime('now', '-25 days'), NULL),

('Introduction à Java Enterprise Edition (Java EE)', 
'Java Enterprise Edition, maintenant connu sous le nom de Jakarta EE, est une plateforme puissante pour le développement d''applications d''entreprise. Dans cet article, nous explorerons les concepts fondamentaux.

## Qu''est-ce que Java EE ?

Java EE fournit un ensemble d''API et de technologies standardisées pour créer des applications distribuées, robustes et scalables. Il s''appuie sur Java SE et ajoute des fonctionnalités pour les applications d''entreprise.

## Composants Principaux

### 1. Servlets et JSP
Les Servlets permettent de créer des applications web dynamiques, tandis que JSP (JavaServer Pages) facilite la création de pages web avec du code Java intégré.

### 2. Enterprise JavaBeans (EJB)
Les EJB fournissent un modèle de composants pour la logique métier, avec support des transactions, de la sécurité et de la concurrence.

### 3. Java Persistence API (JPA)
JPA simplifie l''accès aux données en fournissant une couche d''abstraction pour la persistance des objets Java.

### 4. JavaServer Faces (JSF)
JSF est un framework pour créer des interfaces utilisateur web basées sur des composants.

## Avantages de Java EE

- **Standardisation** : Basé sur des standards ouverts
- **Scalabilité** : Conçu pour les applications d''entreprise
- **Sécurité** : Mécanismes de sécurité intégrés
- **Portabilité** : Fonctionne sur différentes plateformes

Java EE reste une excellente choix pour les applications d''entreprise nécessitant robustesse et scalabilité.',
'Découvrez les bases de Java EE et comment cette plateforme peut vous aider à développer des applications d''entreprise robustes et scalables.',
1, 'PUBLIE', datetime('now', '-22 days'), datetime('now', '-20 days')),

('Les Meilleures Pratiques de Sécurité Web', 
'La sécurité web est un aspect crucial du développement d''applications modernes. Voici un guide complet des meilleures pratiques essentielles.

## 1. Authentification et Autorisation

### Authentification Robuste
- Utilisez des mécanismes d''authentification multi-facteurs (MFA) quand c''est possible
- Implémentez des politiques de mots de passe fortes
- Limitez les tentatives de connexion pour prévenir les attaques par force brute

### Contrôle d''Accès
- Implémentez le principe du moindre privilège
- Utilisez des rôles et permissions granulaires
- Validez les autorisations côté serveur, pas seulement côté client

## 2. Protection des Mots de Passe

**Ne stockez JAMAIS les mots de passe en clair !**

Utilisez des algorithmes de hachage sécurisés :
- **BCrypt** : Recommandé pour la plupart des cas
- **Argon2** : Pour des besoins de sécurité élevés
- **PBKDF2** : Alternative fiable

Exemple avec BCrypt :
```java
String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
```

## 3. Protection CSRF (Cross-Site Request Forgery)

Implémentez des tokens CSRF pour protéger vos formulaires :
- Générez un token unique pour chaque session
- Incluez le token dans tous les formulaires
- Validez le token côté serveur à chaque soumission

## 4. Validation et Échappement des Entrées

- **Validation** : Vérifiez le format et le type de toutes les entrées
- **Échappement** : Échappez les données avant de les afficher
- **Préparation des requêtes** : Utilisez toujours des requêtes préparées pour prévenir les injections SQL

## 5. HTTPS et Chiffrement

- Utilisez **toujours HTTPS en production**
- Configurez correctement les certificats SSL/TLS
- Implémentez HSTS (HTTP Strict Transport Security)
- Chiffrez les données sensibles au repos

## 6. Gestion des Sessions

- Utilisez des identifiants de session sécurisés et aléatoires
- Configurez des timeouts de session appropriés
- Invalidez les sessions après déconnexion
- Protégez contre le vol de session (cookie flags sécurisés)

## 7. Protection XSS (Cross-Site Scripting)

- Échappez toutes les sorties utilisateur
- Utilisez Content Security Policy (CSP)
- Validez et sanitisez les entrées HTML
- Utilisez des bibliothèques comme OWASP Java HTML Sanitizer

En suivant ces pratiques, vous pouvez considérablement améliorer la sécurité de vos applications web.',
'Apprenez les meilleures pratiques de sécurité essentielles pour protéger vos applications web contre les menaces courantes et modernes.',
3, 'PUBLIE', datetime('now', '-20 days'), NULL),

('Guide Complet du Pattern MVC', 
'Le pattern Model-View-Controller (MVC) est l''un des patterns d''architecture les plus utilisés dans le développement web. Comprendre et bien implémenter MVC est essentiel pour créer des applications maintenables.

## Architecture MVC

MVC sépare l''application en trois composants principaux avec des responsabilités distinctes :

### Model (Modèle)
Le modèle représente les données et la logique métier de l''application.

**Responsabilités :**
- Gérer l''accès aux données (base de données, fichiers, APIs)
- Contenir la logique métier et les règles de l''application
- Notifier la vue des changements d''état

**Exemple :**
```java
public class User {
    private Long id;
    private String email;
    private String password;
    // Getters, setters, logique métier
}
```

### View (Vue)
La vue représente l''interface utilisateur et affiche les données du modèle.

**Responsabilités :**
- Présenter les données à l''utilisateur
- Capturer les interactions utilisateur
- Être passive (ne contient pas de logique métier)

**Exemple :**
```jsp
<h1>Bienvenue ${user.name}</h1>
<p>Email: ${user.email}</p>
```

### Controller (Contrôleur)
Le contrôleur gère les interactions entre le modèle et la vue.

**Responsabilités :**
- Recevoir les requêtes utilisateur
- Appeler la logique métier appropriée
- Sélectionner la vue à afficher
- Gérer la navigation

**Exemple :**
```java
@WebServlet("/user")
public class UserController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        User user = userService.findById(id);
        request.setAttribute("user", user);
        request.getRequestDispatcher("/user.jsp").forward(request, response);
    }
}
```

## Avantages du Pattern MVC

1. **Séparation des préoccupations** : Chaque composant a une responsabilité claire
2. **Maintenabilité** : Facilite la maintenance et les modifications
3. **Testabilité** : Permet de tester chaque composant indépendamment
4. **Réutilisabilité** : Les modèles et vues peuvent être réutilisés
5. **Évolutivité** : Facilite l''ajout de nouvelles fonctionnalités

## Bonnes Pratiques

- Ne mettez jamais de logique métier dans la vue
- Le contrôleur doit rester mince (thin controller)
- Utilisez des DTOs (Data Transfer Objects) pour transférer les données
- Implémentez des services pour la logique métier complexe

Cette séparation des responsabilités facilite la maintenance, les tests et l''évolution de l''application.',
'Comprenez le pattern MVC en profondeur et apprenez comment l''implémenter efficacement dans vos projets web pour créer des applications bien structurées.',
3, 'PUBLIE', datetime('now', '-18 days'), NULL),

('SQLite : Guide Complet pour le Développement Web', 
'SQLite est l''un des systèmes de gestion de bases de données relationnelles les plus populaires au monde. Il est particulièrement adapté au développement web pour de nombreuses raisons.

## Qu''est-ce que SQLite ?

SQLite est un moteur de base de données SQL embarqué, léger et sans serveur. Contrairement à MySQL ou PostgreSQL, SQLite ne nécessite pas de processus serveur séparé.

## Caractéristiques Principales

### Avantages
- **Léger** : Bibliothèque de quelques centaines de KB
- **Sans configuration** : Pas besoin de serveur ou de configuration complexe
- **Rapide** : Excellentes performances pour la plupart des applications
- **ACID** : Support complet des transactions ACID
- **Portable** : Un seul fichier de base de données
- **Open Source** : Licence publique du domaine

### Limitations
- Pas de gestion des utilisateurs (pas de système d''authentification)
- Accès concurrent en écriture limité
- Taille maximale recommandée : quelques GB
- Pas idéal pour les applications haute concurrence

## Utilisation dans le Développement Web

### Quand Utiliser SQLite

✅ **Idéal pour :**
- Prototypes et applications de petite à moyenne taille
- Applications avec peu d''accès concurrents en écriture
- Développement et tests
- Applications embarquées
- Sites web avec trafic modéré

❌ **À éviter pour :**
- Applications nécessitant beaucoup d''accès concurrents en écriture
- Applications très volumineuses (plusieurs GB)
- Systèmes nécessitant des utilisateurs multiples avec permissions

## Fonctionnalités SQLite

### Transactions ACID
SQLite supporte complètement les transactions ACID :
```sql
BEGIN TRANSACTION;
INSERT INTO users (name, email) VALUES ('John', 'john@example.com');
UPDATE articles SET status = 'PUBLISHED' WHERE id = 1;
COMMIT;
```

### Clés Étrangères
Depuis SQLite 3.6.19, les clés étrangères sont supportées (nécessitent d''être activées) :
```sql
PRAGMA foreign_keys = ON;
```

### Index
Les index améliorent considérablement les performances :
```sql
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_article_date ON articles(date_creation);
```

## Bonnes Pratiques

1. **Activez les clés étrangères** : `PRAGMA foreign_keys = ON;`
2. **Utilisez des transactions** : Pour les opérations multiples
3. **Créez des index** : Pour les colonnes fréquemment interrogées
4. **Faites des sauvegardes régulières** : Le fichier .db peut être copié directement
5. **Gérez les connexions** : Utilisez un pool de connexions même avec SQLite

## Migration vers une Base de Données Serveur

Quand votre application grandit, vous pouvez migrer vers PostgreSQL ou MySQL. La plupart des requêtes SQL sont compatibles, facilitant la migration.

SQLite est un excellent choix pour démarrer rapidement et évoluer progressivement.',
'Découvrez comment SQLite peut être utilisé efficacement dans vos projets web, ses avantages, limitations et meilleures pratiques.',
1, 'PUBLIE', datetime('now', '-15 days'), NULL),

('Introduction aux Servlets Java', 
'Les Servlets sont la base du développement web en Java. Dans cet article, nous explorerons les concepts fondamentaux des Servlets.

## Qu''est-ce qu''une Servlet ?

Une Servlet est une classe Java qui étend les fonctionnalités d''un serveur web. Elle traite les requêtes HTTP et génère des réponses dynamiques.

## Cycle de Vie d''une Servlet

1. **Chargement et instanciation** : Le conteneur charge la classe
2. **Initialisation** : `init()` est appelée une fois
3. **Traitement des requêtes** : `service()`, `doGet()`, `doPost()` sont appelées
4. **Destruction** : `destroy()` est appelée lors de l''arrêt

## Exemple Basique

```java
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, 
                        HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h1>Hello World!</h1>");
    }
}
```

## Gestion des Sessions

Les sessions permettent de maintenir l''état entre les requêtes :
```java
HttpSession session = request.getSession();
session.setAttribute("user", user);
User user = (User) session.getAttribute("user");
```

Les Servlets sont essentielles pour tout développeur Java web !',
'Apprenez les bases des Servlets Java et comment créer des applications web dynamiques avec cette technologie fondamentale.',
4, 'PUBLIE', datetime('now', '-12 days'), NULL),

('Les Fondamentaux de JSP (JavaServer Pages)', 
'JSP permet de créer des pages web dynamiques en mélangeant HTML et Java. C''est une technologie complémentaire aux Servlets.

## Syntaxe JSP

### Scriptlets
```jsp
<%
    String name = request.getParameter("name");
    out.println("Hello " + name);
%>
```

### Expressions
```jsp
<p>Bienvenue <%= user.getName() %></p>
```

### Directives
```jsp
<%@ page import="java.util.List" %>
<%@ include file="header.jsp" %>
```

## JSTL (JSP Standard Tag Library)

JSTL simplifie le code JSP :
```jsp
<c:forEach var="article" items="${articles}">
    <h2>${article.title}</h2>
    <p>${article.content}</p>
</c:forEach>
```

JSP facilite la création d''interfaces utilisateur dynamiques !',
'Découvrez comment utiliser JSP pour créer des pages web dynamiques et interactives avec Java.',
4, 'PUBLIE', datetime('now', '-10 days'), NULL),

('Guide de Déploiement d''Applications Java Web', 
'Le déploiement d''applications Java web peut sembler complexe, mais avec les bonnes pratiques, c''est tout à fait gérable.

## Formats de Déploiement

### WAR (Web Application Archive)
Un fichier WAR contient toute votre application web :
- Structure standardisée
- Facile à déployer sur Tomcat, Jetty, etc.
- Peut être créé avec Maven ou Gradle

## Serveurs d''Applications

### Apache Tomcat
- Léger et rapide
- Idéal pour les Servlets et JSP
- Configuration simple

### Jetty
- Très léger
- Excellent pour le développement
- Support embarqué

## Variables d''Environnement

Utilisez des variables d''environnement pour la configuration :
- Chemins de base de données
- Clés API
- Paramètres de connexion

## Bonnes Pratiques

1. Externalisez la configuration
2. Utilisez des profils (dev, prod)
3. Configurez correctement les logs
4. Testez en environnement de staging

Le déploiement devient simple avec de la pratique !',
'Apprenez les meilleures pratiques pour déployer vos applications Java web en production de manière efficace et sécurisée.',
2, 'PUBLIE', datetime('now', '-8 days'), NULL),

('Article en Brouillon - À Compléter', 
'Ceci est un article en cours de rédaction. Le contenu sera complété prochainement.

Merci de votre patience.',
'Article en cours de rédaction.',
5, 'BROUILLON', datetime('now', '-5 days'), NULL),

('Article Archivé - Ancien Contenu', 
'Cet article a été archivé car son contenu n''est plus d''actualité. Il est conservé à des fins de référence historique.

Le contenu original traitait des anciennes versions de Java EE.',
'Article archivé pour référence historique.',
1, 'ARCHIVE', datetime('now', '-60 days'), NULL);

-- ============================================
-- INSERT COMMENTS
-- ============================================

INSERT INTO commentaires (contenu, article_id, auteur_id, date_creation, approuve) VALUES
('Excellent article ! Merci pour ce partage. C''est exactement ce dont j''avais besoin pour comprendre Java EE.', 2, 3, datetime('now', '-21 days'), 1),
('Très intéressant, j''ai appris beaucoup de choses. Pourriez-vous faire un article plus détaillé sur JPA ?', 2, 4, datetime('now', '-20 days'), 1),
('La sécurité est effectivement cruciale. Merci pour ces conseils pratiques, je vais les appliquer dans mon projet.', 3, 1, datetime('now', '-19 days'), 1),
('Super article sur la sécurité ! Pourriez-vous approfondir la partie sur les tokens CSRF ?', 3, 2, datetime('now', '-18 days'), 1),
('Le pattern MVC est vraiment essentiel. Cet article explique très bien les concepts. Merci !', 4, 5, datetime('now', '-17 days'), 1),
('J''ai une question : comment gérer les dépendances circulaires entre le modèle et le contrôleur ?', 4, 6, datetime('now', '-16 days'), 1),
('SQLite est effectivement un excellent choix pour démarrer. Merci pour ce guide complet !', 5, 3, datetime('now', '-14 days'), 1),
('Très bon article ! J''utilise SQLite dans mon projet et je confirme que c''est parfait pour les petites applications.', 5, 7, datetime('now', '-13 days'), 1),
('Les Servlets sont la base de tout développement web Java. Merci pour cette introduction claire.', 6, 4, datetime('now', '-11 days'), 1),
('JSP est très pratique pour créer des interfaces. Avez-vous des exemples avec JSTL ?', 7, 5, datetime('now', '-9 days'), 1),
('Excellent guide de déploiement ! Les variables d''environnement sont effectivement essentielles.', 8, 6, datetime('now', '-7 days'), 1),
('Merci pour ces informations. Pourriez-vous faire un article sur le déploiement sur Railway ou Heroku ?', 8, 7, datetime('now', '-6 days'), 1),
('Bienvenue à tous les nouveaux membres ! N''hésitez pas à poser des questions.', 1, 2, datetime('now', '-24 days'), 1),
('Super communauté ! Je suis ravi de faire partie de ce forum.', 1, 3, datetime('now', '-23 days'), 1),
('Commentaire en attente de modération', 2, 8, datetime('now', '-4 days'), 0);

-- ============================================
-- VERIFICATION QUERIES
-- ============================================
-- Uncomment these to verify the data after seeding:

-- SELECT COUNT(*) as total_users FROM utilisateurs;
-- SELECT COUNT(*) as total_articles FROM articles;
-- SELECT COUNT(*) as total_comments FROM commentaires;

-- SELECT u.nom, u.prenom, u.email, u.role, COUNT(a.id) as nb_articles 
-- FROM utilisateurs u 
-- LEFT JOIN articles a ON u.id = a.auteur_id 
-- GROUP BY u.id;

-- SELECT a.titre, a.statut, COUNT(c.id) as nb_comments 
-- FROM articles a 
-- LEFT JOIN commentaires c ON a.id = c.article_id 
-- GROUP BY a.id 
-- ORDER BY a.date_creation DESC;

-- ============================================
-- END OF SEEDERS
-- ============================================
