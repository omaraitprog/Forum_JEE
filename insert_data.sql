-- Script SQL pour ajouter des données dans la base de données après le hosting
-- Utilisez ce script pour insérer des données de test ou des données initiales
-- 
-- IMPORTANT: Assurez-vous d'être connecté à la bonne base de données MySQL
-- USE blog_jee;

-- ============================================
-- INSERTION D'UTILISATEURS
-- ============================================

-- Note: Les mots de passe doivent être hashés avec BCrypt
-- Hash pour "password123": $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

-- Exemple: Ajouter un nouvel administrateur
INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, role, actif, bio) 
VALUES 
('Admin', 'Principal', 'admin@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', 1, 'Administrateur principal du forum');

-- Exemple: Ajouter un nouveau membre
INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, role, actif, bio) 
VALUES 
('Dupont', 'Marie', 'marie.dupont@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'MEMBRE', 1, 'Passionnée de développement web');

-- ============================================
-- INSERTION D'ARTICLES
-- ============================================

-- Note: Remplacez l'auteur_id par l'ID d'un utilisateur existant dans votre base
-- Vous pouvez vérifier les IDs avec: SELECT id, email FROM utilisateurs;

-- Exemple: Ajouter un nouvel article
INSERT INTO articles (titre, contenu, resume, auteur_id, statut, date_creation) 
VALUES 
('Mon Premier Article', 
'Ceci est le contenu de mon premier article. Vous pouvez écrire tout ce que vous voulez ici.

Le contenu peut contenir plusieurs paragraphes et être formaté comme vous le souhaitez.',
'Un résumé court de l''article qui apparaîtra dans la liste des articles.',
1, -- Remplacez par l'ID d'un utilisateur existant
'PUBLIE', 
NOW());

-- Exemple: Ajouter un autre article
INSERT INTO articles (titre, contenu, resume, auteur_id, statut, date_creation) 
VALUES 
('Guide de Développement Web', 
'Dans cet article, nous allons explorer les meilleures pratiques du développement web moderne.

## Les technologies essentielles

1. HTML5 pour la structure
2. CSS3 pour le style
3. JavaScript pour l''interactivité
4. Java EE pour le backend

Ces technologies forment la base d''une application web moderne et performante.',
'Découvrez les technologies essentielles pour développer des applications web modernes.',
1, -- Remplacez par l'ID d'un utilisateur existant
'PUBLIE', 
NOW());

-- ============================================
-- INSERTION DE COMMENTAIRES
-- ============================================

-- Note: Remplacez article_id et auteur_id par des IDs existants
-- Vérifiez les articles avec: SELECT id, titre FROM articles;
-- Vérifiez les utilisateurs avec: SELECT id, email FROM utilisateurs;

-- Exemple: Ajouter un commentaire
INSERT INTO commentaires (contenu, article_id, auteur_id, date_creation, approuve) 
VALUES 
('Excellent article ! Merci pour ces informations utiles.',
1, -- Remplacez par l'ID d'un article existant
2, -- Remplacez par l'ID d'un utilisateur existant
NOW(),
1);

-- ============================================
-- REQUÊTES UTILES POUR VÉRIFIER LES DONNÉES
-- ============================================

-- Voir tous les utilisateurs
-- SELECT id, nom, prenom, email, role, actif FROM utilisateurs;

-- Voir tous les articles
-- SELECT id, titre, auteur_id, statut, date_creation FROM articles;

-- Voir tous les commentaires
-- SELECT id, article_id, auteur_id, date_creation, approuve FROM commentaires;

-- Compter les enregistrements
-- SELECT COUNT(*) as total_utilisateurs FROM utilisateurs;
-- SELECT COUNT(*) as total_articles FROM articles;
-- SELECT COUNT(*) as total_commentaires FROM commentaires;
