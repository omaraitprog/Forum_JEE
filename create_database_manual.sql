-- Script SQL simplifié pour créer la base de données manuellement
-- Copiez-collez ce contenu dans un client SQLite (DB Browser for SQLite, etc.)

-- Table des utilisateurs
CREATE TABLE utilisateurs (
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
    bio TEXT,
    INDEX idx_email (email),
    INDEX idx_token (token_verification)
);

-- Table des articles
CREATE TABLE articles (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titre VARCHAR(255) NOT NULL,
    contenu TEXT NOT NULL,
    resume VARCHAR(500),
    auteur_id INTEGER NOT NULL,
    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_modification DATETIME NULL,
    statut TEXT DEFAULT 'PUBLIE' CHECK(statut IN ('BROUILLON','PUBLIE','ARCHIVE')),
    image_url VARCHAR(255),
    FOREIGN KEY (auteur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    INDEX idx_auteur (auteur_id),
    INDEX idx_statut (statut),
    INDEX idx_date_creation (date_creation)
);

-- Table des commentaires
CREATE TABLE commentaires (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    contenu TEXT NOT NULL,
    article_id INTEGER NOT NULL,
    auteur_id INTEGER NOT NULL,
    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
    approuve INTEGER DEFAULT 1,
    FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE,
    FOREIGN KEY (auteur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    INDEX idx_article (article_id),
    INDEX idx_auteur (auteur_id)
);

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
1, 'PUBLIE', datetime('now', '-5 days')),

('Introduction à Java EE', 
'Java Enterprise Edition (Java EE) est une plateforme de développement d''applications d''entreprise. Elle fournit un ensemble d''API et de technologies pour créer des applications distribuées, robustes et scalables.

Dans cet article, nous allons explorer les concepts fondamentaux de Java EE, notamment :
- Les Servlets et JSP pour le développement web
- Les Enterprise JavaBeans (EJB) pour la logique métier
- Les API de persistance (JPA) pour l''accès aux données
- Les services web REST et SOAP

Java EE facilite le développement d''applications complexes en fournissant des composants réutilisables et des services intégrés.',
'Découvrez les bases de Java EE et comment cette plateforme peut vous aider à développer des applications d''entreprise.',
1, 'PUBLIE', datetime('now', '-4 days')),

('Les meilleures pratiques de sécurité web', 
'La sécurité web est un aspect crucial du développement d''applications modernes. Voici quelques bonnes pratiques essentielles :

1. **Authentification et autorisation** : Utilisez des mécanismes d''authentification robustes et implémentez un contrôle d''accès approprié.

2. **Hachage des mots de passe** : Ne stockez jamais les mots de passe en clair. Utilisez des algorithmes de hachage sécurisés comme BCrypt.

3. **Protection CSRF** : Implémentez des tokens CSRF pour protéger vos formulaires contre les attaques cross-site request forgery.

4. **Validation des entrées** : Validez et échappez toutes les entrées utilisateur pour prévenir les injections SQL et XSS.

5. **HTTPS** : Utilisez toujours HTTPS en production pour chiffrer les communications.

En suivant ces pratiques, vous pouvez considérablement améliorer la sécurité de vos applications web.',
'Apprenez les meilleures pratiques de sécurité pour protéger vos applications web contre les menaces courantes.',
2, 'PUBLIE', datetime('now', '-3 days')),

('Guide complet du développement MVC', 
'Le pattern Model-View-Controller (MVC) est l''un des patterns d''architecture les plus utilisés dans le développement web. Il sépare l''application en trois composants principaux :

**Model (Modèle)** : Représente les données et la logique métier. Il gère l''accès aux données et les règles de l''application.

**View (Vue)** : Représente l''interface utilisateur. Elle affiche les données du modèle à l''utilisateur.

**Controller (Contrôleur)** : Gère les interactions entre le modèle et la vue. Il reçoit les entrées utilisateur et met à jour le modèle en conséquence.

Cette séparation des responsabilités facilite la maintenance, les tests et l''évolution de l''application.',
'Comprenez le pattern MVC et comment l''implémenter efficacement dans vos projets web.',
2, 'PUBLIE', datetime('now', '-2 days')),

('SQLite et la gestion des bases de données', 
'SQLite est l''un des systèmes de gestion de bases de données relationnelles les plus populaires. Il est largement utilisé dans le développement web pour stocker et gérer les données des applications.

Quelques points clés sur SQLite :
- Support des transactions ACID
- Haute performance et légèreté
- Support des index pour optimiser les requêtes
- Gestion des relations entre tables via les clés étrangères
- Base de données embarquée, pas besoin de serveur séparé

Dans ce blog, nous utilisons SQLite pour stocker les utilisateurs, articles et commentaires. La structure relationnelle permet de maintenir l''intégrité des données et de faciliter les requêtes complexes.',
'Découvrez comment SQLite peut être utilisé efficacement dans vos projets web pour gérer les données.',
1, 'PUBLIE', datetime('now', '-1 day'));

-- Commentaires de test
INSERT INTO commentaires (contenu, article_id, auteur_id, date_creation) VALUES
('Excellent article ! Merci pour ce partage.', 1, 2, datetime('now', '-4 days')),
('Très intéressant, j''ai appris beaucoup de choses sur Java EE.', 2, 2, datetime('now', '-3 days')),
('La sécurité est effectivement cruciale. Merci pour ces conseils pratiques.', 3, 1, datetime('now', '-2 days')),
('Le pattern MVC est vraiment essentiel pour bien structurer une application.', 4, 1, datetime('now', '-1 day')),
('SQLite est effectivement un excellent choix pour ce type de projet.', 5, 2, datetime('now', '-12 hours'));
