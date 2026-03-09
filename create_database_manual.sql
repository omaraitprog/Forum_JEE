-- Script SQL pour créer la base de données manuellement dans MySQL
-- Exécutez ce script dans un client MySQL (MySQL Workbench, phpMyAdmin, ligne de commande, etc.)

-- Créer la base de données
CREATE DATABASE IF NOT EXISTS blog_jee
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE blog_jee;

-- Table des utilisateurs
CREATE TABLE IF NOT EXISTS utilisateurs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    role ENUM('MEMBRE','ADMIN') DEFAULT 'MEMBRE',
    actif TINYINT(1) DEFAULT 0,
    token_verification VARCHAR(255),
    date_inscription DATETIME DEFAULT CURRENT_TIMESTAMP,
    photo_profil VARCHAR(255),
    bio TEXT,
    INDEX idx_email (email),
    INDEX idx_token (token_verification)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table des articles
CREATE TABLE IF NOT EXISTS articles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    contenu TEXT NOT NULL,
    resume VARCHAR(500),
    auteur_id INT NOT NULL,
    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_modification DATETIME NULL,
    statut ENUM('BROUILLON','PUBLIE','ARCHIVE') DEFAULT 'PUBLIE',
    image_url VARCHAR(255),
    FOREIGN KEY (auteur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    INDEX idx_auteur (auteur_id),
    INDEX idx_statut (statut),
    INDEX idx_date_creation (date_creation)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table des commentaires
CREATE TABLE IF NOT EXISTS commentaires (
    id INT AUTO_INCREMENT PRIMARY KEY,
    contenu TEXT NOT NULL,
    article_id INT NOT NULL,
    auteur_id INT NOT NULL,
    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
    approuve TINYINT(1) DEFAULT 1,
    FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE,
    FOREIGN KEY (auteur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    INDEX idx_article (article_id),
    INDEX idx_auteur_comment (auteur_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Données de test
-- Mots de passe hashés avec BCrypt (mot de passe = "password123" pour tous)
-- Hash: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

-- Utilisateur 1 (Admin, actif)
INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, role, actif, bio) VALUES
('Dupont', 'Jean', 'admin@blog.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', 1, 'Administrateur du blog');

-- Utilisateur 2 (Membre, actif)
INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, role, actif, bio) VALUES
('Martin', 'Sophie', 'sophie@blog.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'MEMBRE', 1, 'Passionnée de technologie et de développement web');

-- Articles de test
INSERT INTO articles (titre, contenu, resume, auteur_id, statut, date_creation) VALUES
('Bienvenue sur notre Blog', 
'Ceci est le premier article de notre blog. Nous sommes ravis de vous accueillir dans cette communauté passionnante où nous partageons nos idées, nos expériences et nos connaissances.

Notre blog couvre de nombreux sujets intéressants, notamment la technologie, le développement web, et bien plus encore. Nous espérons que vous trouverez du contenu qui vous intéresse et que vous participerez activement à nos discussions.

N''hésitez pas à commenter les articles et à partager vos propres réflexions avec la communauté !',
'Un message de bienvenue pour tous les nouveaux membres de notre communauté de blog.',
1, 'PUBLIE', DATE_SUB(NOW(), INTERVAL 5 DAY)),

('Introduction à Java EE', 
'Java Enterprise Edition (Java EE) est une plateforme de développement d''applications d''entreprise. Elle fournit un ensemble d''API et de technologies pour créer des applications distribuées, robustes et scalables.

Dans cet article, nous allons explorer les concepts fondamentaux de Java EE, notamment :
- Les Servlets et JSP pour le développement web
- Les Enterprise JavaBeans (EJB) pour la logique métier
- Les API de persistance (JPA) pour l''accès aux données
- Les services web REST et SOAP

Java EE facilite le développement d''applications complexes en fournissant des composants réutilisables et des services intégrés.',
'Découvrez les bases de Java EE et comment cette plateforme peut vous aider à développer des applications d''entreprise.',
1, 'PUBLIE', DATE_SUB(NOW(), INTERVAL 4 DAY)),

('Les meilleures pratiques de sécurité web', 
'La sécurité web est un aspect crucial du développement d''applications modernes. Voici quelques bonnes pratiques essentielles :

1. **Authentification et autorisation** : Utilisez des mécanismes d''authentification robustes et implémentez un contrôle d''accès approprié.

2. **Hachage des mots de passe** : Ne stockez jamais les mots de passe en clair. Utilisez des algorithmes de hachage sécurisés comme BCrypt.

3. **Protection CSRF** : Implémentez des tokens CSRF pour protéger vos formulaires contre les attaques cross-site request forgery.

4. **Validation des entrées** : Validez et échappez toutes les entrées utilisateur pour prévenir les injections SQL et XSS.

5. **HTTPS** : Utilisez toujours HTTPS en production pour chiffrer les communications.

En suivant ces pratiques, vous pouvez considérablement améliorer la sécurité de vos applications web.',
'Apprenez les meilleures pratiques de sécurité pour protéger vos applications web contre les menaces courantes.',
2, 'PUBLIE', DATE_SUB(NOW(), INTERVAL 3 DAY)),

('Guide complet du développement MVC', 
'Le pattern Model-View-Controller (MVC) est l''un des patterns d''architecture les plus utilisés dans le développement web. Il sépare l''application en trois composants principaux :

**Model (Modèle)** : Représente les données et la logique métier. Il gère l''accès aux données et les règles de l''application.

**View (Vue)** : Représente l''interface utilisateur. Elle affiche les données du modèle à l''utilisateur.

**Controller (Contrôleur)** : Gère les interactions entre le modèle et la vue. Il reçoit les entrées utilisateur et met à jour le modèle en conséquence.

Cette séparation des responsabilités facilite la maintenance, les tests et l''évolution de l''application.',
'Comprenez le pattern MVC et comment l''implémenter efficacement dans vos projets web.',
2, 'PUBLIE', DATE_SUB(NOW(), INTERVAL 2 DAY)),

('MySQL et la gestion des bases de données', 
'MySQL est l''un des systèmes de gestion de bases de données relationnelles les plus populaires. Il est largement utilisé dans le développement web pour stocker et gérer les données des applications.

Quelques points clés sur MySQL :
- Support des transactions ACID
- Haute performance et scalabilité
- Support des index pour optimiser les requêtes
- Gestion des relations entre tables via les clés étrangères
- Gestion avancée des utilisateurs et permissions

Dans ce blog, nous utilisons MySQL pour stocker les utilisateurs, articles et commentaires. La structure relationnelle permet de maintenir l''intégrité des données et de faciliter les requêtes complexes.',
'Découvrez comment MySQL peut être utilisé efficacement dans vos projets web pour gérer les données.',
1, 'PUBLIE', DATE_SUB(NOW(), INTERVAL 1 DAY));

-- Commentaires de test
INSERT INTO commentaires (contenu, article_id, auteur_id, date_creation) VALUES
('Excellent article ! Merci pour ce partage.', 1, 2, DATE_SUB(NOW(), INTERVAL 4 DAY)),
('Très intéressant, j''ai appris beaucoup de choses sur Java EE.', 2, 2, DATE_SUB(NOW(), INTERVAL 3 DAY)),
('La sécurité est effectivement cruciale. Merci pour ces conseils pratiques.', 3, 1, DATE_SUB(NOW(), INTERVAL 2 DAY)),
('Le pattern MVC est vraiment essentiel pour bien structurer une application.', 4, 1, DATE_SUB(NOW(), INTERVAL 1 DAY)),
('MySQL est effectivement un excellent choix pour ce type de projet.', 5, 2, DATE_SUB(NOW(), INTERVAL 12 HOUR));
