-- ============================================
-- SEEDERS - STATIC DATA FOR MYSQL DATABASE
-- ============================================
-- This file contains static seed data for the Forum/Blog application
-- Execute this script after creating the database schema
-- 
-- IMPORTANT: Make sure to USE the correct database
-- USE blog_jee;
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
('Admin', 'Principal', 'admin@forum.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', 1, 'Administrateur principal du forum. Passionné de technologie et de développement web.', DATE_SUB(NOW(), INTERVAL 30 DAY)),
('Dupont', 'Jean', 'jean.dupont@forum.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', 1, 'Co-administrateur et développeur full-stack avec 10 ans d''expérience.', DATE_SUB(NOW(), INTERVAL 25 DAY)),
('Martin', 'Sophie', 'sophie.martin@forum.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'MEMBRE', 1, 'Développeuse Java passionnée par les architectures d''entreprise et les bonnes pratiques.', DATE_SUB(NOW(), INTERVAL 20 DAY)),
('Bernard', 'Pierre', 'pierre.bernard@forum.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'MEMBRE', 1, 'Expert en sécurité web et en développement d''applications sécurisées.', DATE_SUB(NOW(), INTERVAL 18 DAY)),
('Dubois', 'Marie', 'marie.dubois@forum.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'MEMBRE', 1, 'Designer UI/UX et développeuse frontend. Amoureuse du design moderne et des interfaces intuitives.', DATE_SUB(NOW(), INTERVAL 15 DAY)),
('Lefebvre', 'Thomas', 'thomas.lefebvre@forum.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'MEMBRE', 1, 'Développeur backend spécialisé en Java EE et microservices.', DATE_SUB(NOW(), INTERVAL 12 DAY)),
('Moreau', 'Julie', 'julie.moreau@forum.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'MEMBRE', 1, 'Étudiante en informatique passionnée par le développement web et les nouvelles technologies.', DATE_SUB(NOW(), INTERVAL 10 DAY)),
('Petit', 'Lucas', 'lucas.petit@forum.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'MEMBRE', 0, 'Nouveau membre de la communauté.', DATE_SUB(NOW(), INTERVAL 5 DAY));

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
1, 'PUBLIE', DATE_SUB(NOW(), INTERVAL 25 DAY), NULL),

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
1, 'PUBLIE', DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY)),

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
3, 'PUBLIE', DATE_SUB(NOW(), INTERVAL 20 DAY), NULL),

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
3, 'PUBLIE', DATE_SUB(NOW(), INTERVAL 18 DAY), NULL),

('MySQL : Guide Complet pour le Développement Web', 
'MySQL est l''un des systèmes de gestion de bases de données relationnelles les plus populaires au monde. Il est particulièrement adapté au développement web pour de nombreuses raisons.

## Qu''est-ce que MySQL ?

MySQL est un SGBDR open source performant et fiable, utilisé par des millions d''applications à travers le monde.

## Caractéristiques Principales

### Avantages
- **Performant** : Excellentes performances pour les applications web
- **Scalable** : Peut gérer de très gros volumes de données
- **Fiable** : Support complet des transactions ACID avec InnoDB
- **Communauté** : Large communauté et excellent support
- **Open Source** : Gratuit et open source

### Fonctionnalités
- Gestion des utilisateurs et permissions
- Accès concurrent en écriture robuste
- Réplication et haute disponibilité
- Idéal pour les applications haute concurrence

## Utilisation dans le Développement Web

### Quand Utiliser MySQL

✅ **Idéal pour :**
- Applications web de toutes tailles
- Applications nécessitant des accès concurrents
- Applications avec beaucoup de données
- Systèmes nécessitant des utilisateurs multiples avec permissions

## Bonnes Pratiques

1. **Utilisez InnoDB** : Pour le support des transactions ACID
2. **Utilisez des transactions** : Pour les opérations multiples
3. **Créez des index** : Pour les colonnes fréquemment interrogées
4. **Faites des sauvegardes régulières** : Avec mysqldump ou des outils dédiés
5. **Gérez les connexions** : Utilisez un pool de connexions

MySQL est un excellent choix pour les applications web de toutes tailles.',
'Découvrez comment MySQL peut être utilisé efficacement dans vos projets web, ses avantages et meilleures pratiques.',
1, 'PUBLIE', DATE_SUB(NOW(), INTERVAL 15 DAY), NULL),

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
4, 'PUBLIE', DATE_SUB(NOW(), INTERVAL 12 DAY), NULL),

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
4, 'PUBLIE', DATE_SUB(NOW(), INTERVAL 10 DAY), NULL),

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
2, 'PUBLIE', DATE_SUB(NOW(), INTERVAL 8 DAY), NULL),

('Article en Brouillon - À Compléter', 
'Ceci est un article en cours de rédaction. Le contenu sera complété prochainement.

Merci de votre patience.',
'Article en cours de rédaction.',
5, 'BROUILLON', DATE_SUB(NOW(), INTERVAL 5 DAY), NULL),

('Article Archivé - Ancien Contenu', 
'Cet article a été archivé car son contenu n''est plus d''actualité. Il est conservé à des fins de référence historique.

Le contenu original traitait des anciennes versions de Java EE.',
'Article archivé pour référence historique.',
1, 'ARCHIVE', DATE_SUB(NOW(), INTERVAL 60 DAY), NULL);

-- ============================================
-- INSERT COMMENTS
-- ============================================

INSERT INTO commentaires (contenu, article_id, auteur_id, date_creation, approuve) VALUES
('Excellent article ! Merci pour ce partage. C''est exactement ce dont j''avais besoin pour comprendre Java EE.', 2, 3, DATE_SUB(NOW(), INTERVAL 21 DAY), 1),
('Très intéressant, j''ai appris beaucoup de choses. Pourriez-vous faire un article plus détaillé sur JPA ?', 2, 4, DATE_SUB(NOW(), INTERVAL 20 DAY), 1),
('La sécurité est effectivement cruciale. Merci pour ces conseils pratiques, je vais les appliquer dans mon projet.', 3, 1, DATE_SUB(NOW(), INTERVAL 19 DAY), 1),
('Super article sur la sécurité ! Pourriez-vous approfondir la partie sur les tokens CSRF ?', 3, 2, DATE_SUB(NOW(), INTERVAL 18 DAY), 1),
('Le pattern MVC est vraiment essentiel. Cet article explique très bien les concepts. Merci !', 4, 5, DATE_SUB(NOW(), INTERVAL 17 DAY), 1),
('J''ai une question : comment gérer les dépendances circulaires entre le modèle et le contrôleur ?', 4, 6, DATE_SUB(NOW(), INTERVAL 16 DAY), 1),
('MySQL est effectivement un excellent choix pour le développement web. Merci pour ce guide complet !', 5, 3, DATE_SUB(NOW(), INTERVAL 14 DAY), 1),
('Très bon article ! J''utilise MySQL dans mon projet et je confirme que c''est parfait pour les applications web.', 5, 7, DATE_SUB(NOW(), INTERVAL 13 DAY), 1),
('Les Servlets sont la base de tout développement web Java. Merci pour cette introduction claire.', 6, 4, DATE_SUB(NOW(), INTERVAL 11 DAY), 1),
('JSP est très pratique pour créer des interfaces. Avez-vous des exemples avec JSTL ?', 7, 5, DATE_SUB(NOW(), INTERVAL 9 DAY), 1),
('Excellent guide de déploiement ! Les variables d''environnement sont effectivement essentielles.', 8, 6, DATE_SUB(NOW(), INTERVAL 7 DAY), 1),
('Merci pour ces informations. Pourriez-vous faire un article sur le déploiement sur le cloud ?', 8, 7, DATE_SUB(NOW(), INTERVAL 6 DAY), 1),
('Bienvenue à tous les nouveaux membres ! N''hésitez pas à poser des questions.', 1, 2, DATE_SUB(NOW(), INTERVAL 24 DAY), 1),
('Super communauté ! Je suis ravi de faire partie de ce forum.', 1, 3, DATE_SUB(NOW(), INTERVAL 23 DAY), 1),
('Commentaire en attente de modération', 2, 8, DATE_SUB(NOW(), INTERVAL 4 DAY), 0);

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
