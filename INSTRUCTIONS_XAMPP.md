# Instructions pour créer la base de données avec XAMPP

## Méthode 1 : phpMyAdmin (RECOMMANDÉ - Le plus simple)

1. **Démarrez XAMPP**
   - Ouvrez le Panneau de contrôle XAMPP
   - Démarrez le service **MySQL** (cliquez sur "Start")

2. **Ouvrez phpMyAdmin**
   - Allez dans votre navigateur : `http://localhost/phpmyadmin`
   - Connectez-vous (généralement pas de mot de passe pour root dans XAMPP)

3. **Exécutez le script SQL**
   - Cliquez sur l'onglet **"SQL"** en haut
   - Ouvrez le fichier `create_database_manual.sql` dans un éditeur de texte
   - Copiez **TOUT** le contenu (Ctrl+A, Ctrl+C)
   - Collez dans la zone SQL de phpMyAdmin
   - Cliquez sur **"Exécuter"** (ou appuyez sur F5)

4. **Vérifiez la création**
   - Dans le menu de gauche, vous devriez voir la base `blog_jee`
   - Cliquez dessus pour voir les tables : `utilisateurs`, `articles`, `commentaires`

## Méthode 2 : Ligne de commande

1. **Double-cliquez** sur le fichier `create_database_xampp.bat`
2. Entrez votre mot de passe MySQL root (ou appuyez sur Entrée si vide)
3. La base de données sera créée automatiquement

## Méthode 3 : MySQL Workbench

1. Ouvrez MySQL Workbench
2. Créez une nouvelle connexion :
   - Hostname: `localhost`
   - Port: `3306`
   - Username: `root`
   - Password: (laissez vide ou votre mot de passe)
3. Connectez-vous
4. Ouvrez le fichier `create_database_manual.sql`
5. Copiez-collez le contenu dans un nouvel onglet SQL
6. Exécutez le script (⚡ ou Ctrl+Shift+Enter)

## Vérification

Après avoir créé la base de données, vérifiez dans phpMyAdmin ou MySQL Workbench :

```sql
SHOW DATABASES;
USE blog_jee;
SHOW TABLES;
SELECT COUNT(*) FROM utilisateurs;
SELECT COUNT(*) FROM articles;
```

Vous devriez voir :
- Base de données `blog_jee`
- 3 tables : `utilisateurs`, `articles`, `commentaires`
- 2 utilisateurs dans la table `utilisateurs`
- 5 articles dans la table `articles`
- 5 commentaires dans la table `commentaires`

## Configuration de l'application

Une fois la base créée, vérifiez que les paramètres de connexion dans `DBConnection.java` sont corrects :

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/blog_jee?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "";  // Laissez vide si pas de mot de passe dans XAMPP
```

## Comptes de test

Après la création, vous pouvez vous connecter avec :

**Administrateur :**
- Email : `admin@blog.com`
- Mot de passe : `password123`

**Membre :**
- Email : `sophie@blog.com`
- Mot de passe : `password123`

## Dépannage

### Erreur : "Access denied"
- Vérifiez que le mot de passe MySQL est correct
- Dans XAMPP, le mot de passe root est souvent vide par défaut

### Erreur : "Can't connect to MySQL server"
- Vérifiez que MySQL est démarré dans XAMPP
- Vérifiez que le port 3306 n'est pas utilisé par un autre service

### Erreur : "Database already exists"
- La base existe déjà, c'est normal
- Le script utilise `CREATE DATABASE IF NOT EXISTS`, donc c'est sans danger
