# Guide : Comment ajouter des données dans la base de données après le hosting

Ce guide vous explique les différentes méthodes pour ajouter des données dans votre base de données MySQL après le déploiement sur Railway (ou tout autre plateforme de hosting).

## 📋 Table des matières

1. [Méthode 1 : Interface d'administration web](#méthode-1--interface-dadministration-web)
2. [Méthode 2 : Script SQL direct](#méthode-2--script-sql-direct)
3. [Méthode 3 : Client MySQL externe](#méthode-3--client-mysql-externe)
4. [Méthode 4 : Via l'application (inscription normale)](#méthode-4--via-lapplication-inscription-normale)

---

## Méthode 1 : Interface d'administration web

### ✅ Avantages
- Interface graphique conviviale
- Pas besoin de connaître SQL
- Validation automatique des données
- Hashage automatique des mots de passe

### 📝 Étapes

1. **Connectez-vous en tant qu'administrateur**
   - Accédez à votre application déployée
   - Connectez-vous avec un compte administrateur

2. **Accédez à la page d'administration**
   - URL : `https://votre-app.railway.app/admin/add-data`
   - Ou : `https://votre-app.railway.app/admin/add-data`

3. **Ajouter un utilisateur**
   - Cliquez sur l'onglet "Ajouter un utilisateur"
   - Remplissez le formulaire :
     - Nom et Prénom (obligatoires)
     - Email (obligatoire, doit être unique)
     - Mot de passe (obligatoire, sera automatiquement hashé)
     - Rôle (MEMBRE ou ADMIN)
     - Biographie (optionnel)
   - Cliquez sur "Créer l'utilisateur"

4. **Ajouter un article**
   - Cliquez sur l'onglet "Ajouter un article"
   - Remplissez le formulaire :
     - Titre (obligatoire)
     - Résumé (optionnel)
     - Contenu (obligatoire)
     - Auteur (sélectionnez parmi les utilisateurs existants)
     - Statut (PUBLIE, BROUILLON, ou ARCHIVE)
     - URL de l'image (optionnel)
   - Cliquez sur "Créer l'article"

### 🔒 Sécurité
- Cette page est protégée et accessible uniquement aux administrateurs
- Si vous n'êtes pas connecté en tant qu'admin, vous recevrez une erreur 403

---

## Méthode 2 : Script SQL direct

### ✅ Avantages
- Rapide pour insérer plusieurs données
- Idéal pour les données de test
- Contrôle total sur les données

### 📝 Étapes

1. **Accédez à l'interface MySQL de Railway**
   - Connectez-vous à votre projet Railway
   - Cliquez sur votre service MySQL
   - Allez dans l'onglet "Query" ou "Data"

2. **Utilisez le script `insert_data.sql`**
   - Ouvrez le fichier `insert_data.sql` dans votre projet
   - Copiez les commandes SQL que vous souhaitez exécuter
   - Collez-les dans l'éditeur SQL de Railway
   - Cliquez sur "Run" ou "Execute"

3. **Exemple d'insertion d'utilisateur**
   ```sql
   INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, role, actif, bio) 
   VALUES 
   ('Dupont', 'Jean', 'jean.dupont@example.com', 
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 
    'ADMIN', TRUE, 'Administrateur du forum');
   ```

4. **Exemple d'insertion d'article**
   ```sql
   INSERT INTO articles (titre, contenu, resume, auteur_id, statut, date_creation) 
   VALUES 
   ('Mon Premier Article', 
    'Ceci est le contenu de mon article...',
    'Résumé de l''article',
    1, -- Remplacez par l'ID d'un utilisateur existant
    'PUBLIE', 
    NOW());
   ```

### ⚠️ Important
- **Mots de passe** : Les mots de passe doivent être hashés avec BCrypt
  - Hash pour "password123" : `$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy`
  - Pour générer un nouveau hash, utilisez l'interface d'administration ou un outil BCrypt

- **IDs** : Assurez-vous que les IDs référencés (auteur_id, etc.) existent dans la base de données

---

## Méthode 3 : Client MySQL externe

### ✅ Avantages
- Interface complète de gestion de base de données
- Visualisation des données
- Exécution de requêtes complexes

### 📝 Étapes

1. **Récupérez les informations de connexion**
   - Dans Railway, allez dans votre service MySQL
   - Cliquez sur l'onglet "Variables"
   - Notez les valeurs suivantes :
     - `MYSQLHOST` (ou `MYSQL_HOST`)
     - `MYSQLPORT` (ou `MYSQL_PORT`)
     - `MYSQLDATABASE` (ou `MYSQL_DATABASE`)
     - `MYSQLUSER` (ou `MYSQL_USER`)
     - `MYSQLPASSWORD` (ou `MYSQL_PASSWORD`)

2. **Installez un client MySQL**
   - **MySQL Workbench** (recommandé) : https://dev.mysql.com/downloads/workbench/
   - **DBeaver** (gratuit) : https://dbeaver.io/
   - **phpMyAdmin** (si disponible)
   - **HeidiSQL** (Windows) : https://www.heidisql.com/

3. **Connectez-vous**
   - Ouvrez votre client MySQL
   - Créez une nouvelle connexion avec les informations de Railway
   - Connectez-vous

4. **Ajoutez des données**
   - Naviguez vers les tables (`utilisateurs`, `articles`, `commentaires`)
   - Utilisez l'interface graphique pour insérer des données
   - Ou exécutez des requêtes SQL directement

### 🔐 Exemple de connexion MySQL Workbench
```
Hostname: containers-us-west-xxx.railway.app
Port: 3306
Username: root
Password: [votre mot de passe Railway]
Default Schema: railway (ou le nom de votre base)
```

---

## Méthode 4 : Via l'application (inscription normale)

### ✅ Avantages
- Processus normal pour les utilisateurs
- Vérification email automatique
- Interface utilisateur standard

### 📝 Étapes

1. **Inscription d'un nouvel utilisateur**
   - Accédez à la page d'inscription : `/inscription`
   - Remplissez le formulaire
   - L'utilisateur recevra un email de vérification
   - Une fois vérifié, l'utilisateur peut se connecter

2. **Création d'articles**
   - Connectez-vous avec un compte utilisateur
   - Allez sur `/articles?action=create`
   - Créez un nouvel article

### ⚠️ Note
- Cette méthode suit le flux normal de l'application
- Les utilisateurs doivent vérifier leur email (sauf si vous les activez manuellement en base)

---

## 🔍 Vérifier les données insérées

### Via SQL
```sql
-- Voir tous les utilisateurs
SELECT id, nom, prenom, email, role, actif FROM utilisateurs;

-- Voir tous les articles
SELECT id, titre, auteur_id, statut, date_creation FROM articles;

-- Voir tous les commentaires
SELECT id, article_id, auteur_id, date_creation FROM commentaires;

-- Compter les enregistrements
SELECT COUNT(*) as total_utilisateurs FROM utilisateurs;
SELECT COUNT(*) as total_articles FROM articles;
SELECT COUNT(*) as total_commentaires FROM commentaires;
```

### Via l'interface web
- Consultez la liste des articles sur la page d'accueil
- Consultez les profils utilisateurs
- Utilisez l'interface d'administration pour voir les données

---

## 🛠️ Générer un hash BCrypt pour un mot de passe

Si vous devez insérer un utilisateur directement en SQL, vous avez besoin d'un hash BCrypt.

### Option 1 : Via l'interface d'administration
- Créez un utilisateur temporaire via l'interface
- Récupérez le hash depuis la base de données

### Option 2 : Via un outil en ligne
- Utilisez un générateur BCrypt en ligne (recherchez "bcrypt hash generator")
- Entrez votre mot de passe
- Copiez le hash généré

### Option 3 : Via Java (dans votre code)
```java
import org.mindrot.jbcrypt.BCrypt;

String password = "votre_mot_de_passe";
String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
System.out.println(hashed);
```

---

## 📚 Ressources utiles

- [Documentation Railway MySQL](https://docs.railway.app/databases/mysql)
- [Documentation MySQL](https://dev.mysql.com/doc/)
- [BCrypt Java Library](https://github.com/jeremyh/jBCrypt)

---

## ❓ Questions fréquentes

### Q: Comment puis-je créer le premier administrateur ?
**R:** Vous pouvez :
1. Utiliser l'interface d'administration si vous avez déjà un admin
2. Insérer directement en SQL avec le rôle 'ADMIN'
3. Modifier un utilisateur existant pour lui donner le rôle ADMIN

### Q: Les données sont-elles persistantes sur Railway ?
**R:** Oui, les données MySQL sur Railway sont persistantes. Elles ne sont pas perdues lors des redéploiements.

### Q: Puis-je exporter mes données ?
**R:** Oui, vous pouvez exporter vos données via :
- L'interface MySQL de Railway (onglet "Data" → "Export")
- Un client MySQL externe (export SQL)
- La commande `mysqldump` si vous avez accès SSH

### Q: Comment puis-je supprimer des données ?
**R:** Vous pouvez :
- Utiliser SQL : `DELETE FROM table WHERE condition;`
- Via un client MySQL externe
- Créer une interface d'administration pour la suppression (non incluse par défaut)

---

**Bon ajout de données ! 🚀**
