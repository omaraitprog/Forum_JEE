# Blog/Forum JEE - Projet Java Enterprise Edition

Un projet web complet de type Blog/Forum développé avec Java EE (Jakarta EE), utilisant Servlets, JSP, JSTL, MySQL et Maven.

## 📋 Table des matières

- [Fonctionnalités](#fonctionnalités)
- [Technologies utilisées](#technologies-utilisées)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Configuration](#configuration)
- [Structure du projet](#structure-du-projet)
- [Utilisation](#utilisation)
- [Sécurité](#sécurité)

## ✨ Fonctionnalités

### 🔐 Authentification et Inscription
- Inscription avec validation des champs
- Connexion/Déconnexion
- Vérification par email (activation du compte)
- Gestion des sessions (timeout de 30 minutes)
- Hachage des mots de passe avec BCrypt

### 📝 Gestion des Articles
- Création, modification, suppression d'articles
- Liste paginée des articles (10 par page)
- Affichage détaillé d'un article
- Statuts : Brouillon, Publié, Archivé
- Images pour les articles

### 💬 Commentaires
- Ajout de commentaires sur les articles
- Suppression de commentaires (auteur ou admin)
- Affichage des commentaires avec auteur et date

### 👤 Profil Utilisateur
- Modification des informations personnelles
- Changement de mot de passe
- Photo de profil
- Biographie

### 🌍 Internationalisation (i18n)
- Support français et anglais
- Changement de langue via menu déroulant
- Conservation de la langue en session

### 🔒 Sécurité
- Protection des routes sensibles (AuthFilter)
- Validation et échappement des entrées utilisateur
- Protection XSS avec `<c:out>`
- Mots de passe hashés avec BCrypt
- Sessions sécurisées

## 🛠 Technologies utilisées

- **Java 21** - Langage de programmation
- **Jakarta EE** (anciennement Java EE)
- **Servlets** - Contrôleurs
- **JSP** - Vues
- **JSTL** - Bibliothèque de tags
- **MySQL** - Base de données
- **Maven** - Gestion des dépendances
- **Apache Tomcat 9+** - Serveur d'application
- **Bootstrap 5** - Framework CSS
- **Font Awesome** - Icônes
- **BCrypt** - Hachage des mots de passe
- **JavaMail** - Envoi d'emails

## 📦 Prérequis

Avant de commencer, assurez-vous d'avoir installé :

- **Java JDK 21** ou supérieur
- **Apache Maven 3.6+**
- **Apache Tomcat 9+** ou supérieur
- **MySQL 8.0+**
- **IDE** (IntelliJ IDEA, Eclipse, ou VS Code)

## 🚀 Installation

### 1. Cloner ou télécharger le projet

```bash
cd Forum_Project
```

### 2. Créer la base de données MySQL

Connectez-vous à MySQL et exécutez le script `schema.sql` :

```bash
mysql -u root -p < schema.sql
```

Ou via MySQL Workbench :
1. Ouvrez MySQL Workbench
2. Connectez-vous à votre serveur MySQL
3. Ouvrez le fichier `schema.sql`
4. Exécutez le script

### 3. Configurer la connexion à la base de données

Modifiez les paramètres dans `src/main/java/com/example/forum_project/utils/DBConnection.java` :

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/blog_jee?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
private static final String DB_USER = "root";  // Votre utilisateur MySQL
private static final String DB_PASSWORD = "";  // Votre mot de passe MySQL
```

### 4. Configurer l'envoi d'emails (optionnel)

Pour activer la vérification par email, modifiez `src/main/java/com/example/forum_project/utils/EmailService.java` :

```java
private static final String SMTP_USER = "votre-email@gmail.com";
private static final String SMTP_PASSWORD = "votre-mot-de-passe-app"; // Mot de passe d'application Gmail
```

**Note** : Pour Gmail, vous devez créer un "Mot de passe d'application" dans les paramètres de sécurité de votre compte Google.

### 5. Compiler le projet

```bash
mvn clean compile
```

### 6. Créer le fichier WAR

```bash
mvn clean package
```

Le fichier WAR sera généré dans le dossier `target/Forum_Project-1.0-SNAPSHOT.war`

### 7. Déployer sur Tomcat

#### Option A : Déploiement manuel
1. Copiez le fichier WAR dans le dossier `webapps` de Tomcat
2. Démarrez Tomcat
3. Accédez à `http://localhost:8080/Forum_Project`

#### Option B : Via IntelliJ IDEA
1. Ouvrez le projet dans IntelliJ IDEA
2. Configurez Tomcat comme serveur d'application
3. Démarrez l'application depuis l'IDE

## ⚙️ Configuration

### Comptes de test

Le script SQL crée deux comptes de test :

1. **Administrateur**
   - Email : `admin@blog.com`
   - Mot de passe : `password123`
   - Rôle : ADMIN

2. **Membre**
   - Email : `sophie@blog.com`
   - Mot de passe : `password123`
   - Rôle : MEMBRE

**Note** : Ces comptes sont déjà activés dans la base de données.

## 📁 Structure du projet

```
Forum_Project/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/forum_project/
│       │       ├── controllers/      # Servlets
│       │       ├── models/           # POJO (Utilisateur, Article, Commentaire)
│       │       ├── dao/              # Data Access Objects
│       │       ├── services/         # Services métier
│       │       ├── filters/          # Filtres (Auth, Langue)
│       │       └── utils/            # Utilitaires (DBConnection, EmailService)
│       ├── resources/
│       │   ├── messages_fr.properties  # Traductions françaises
│       │   └── messages_en.properties  # Traductions anglaises
│       └── webapp/
│           ├── WEB-INF/
│           │   ├── web.xml           # Configuration web
│           │   └── views/            # Pages JSP
│           │       ├── auth/         # Pages d'authentification
│           │       ├── articles/     # Pages des articles
│           │       ├── profile/      # Page de profil
│           │       ├── includes/     # Header, Navbar, Footer
│           │       └── error/       # Pages d'erreur
│           ├── css/
│           │   └── style.css         # Styles personnalisés
│           └── js/
│               └── main.js           # Scripts JavaScript
├── schema.sql                        # Script SQL de création de la base
├── pom.xml                           # Configuration Maven
└── README.md                         # Ce fichier
```

## 🎯 Utilisation

### Accès à l'application

Une fois déployée, accédez à :
- **URL principale** : `http://localhost:8080/Forum_Project`
- **Liste des articles** : `http://localhost:8080/Forum_Project/articles`
- **Connexion** : `http://localhost:8080/Forum_Project/login`
- **Inscription** : `http://localhost:8080/Forum_Project/inscription`

### Workflow utilisateur

1. **Inscription** : Créez un compte via la page d'inscription
2. **Vérification** : Cliquez sur le lien dans l'email de confirmation (si configuré)
3. **Connexion** : Connectez-vous avec vos identifiants
4. **Création d'articles** : Créez et publiez des articles
5. **Commentaires** : Commentez les articles
6. **Profil** : Modifiez votre profil et changez votre mot de passe

### Changement de langue

Utilisez le menu déroulant dans la barre de navigation pour changer entre français et anglais.

## 🔒 Sécurité

### Mesures de sécurité implémentées

1. **Hachage des mots de passe** : Utilisation de BCrypt (salt automatique)
2. **Protection des routes** : Filtre d'authentification pour les pages protégées
3. **Validation des entrées** : Validation côté client et serveur
4. **Échappement XSS** : Utilisation de `<c:out>` dans les JSP
5. **Sessions sécurisées** : Timeout de 30 minutes, cookies HTTP-only
6. **Vérification des permissions** : Seuls les auteurs ou admins peuvent modifier/supprimer

### Recommandations pour la production

- Utiliser HTTPS
- Configurer un pool de connexions (HikariCP, C3P0)
- Implémenter une protection CSRF plus robuste
- Ajouter un système de logs
- Configurer un reverse proxy (Nginx)
- Mettre en place un système de backup de la base de données

## 🐛 Dépannage

### Problème de connexion à la base de données

- Vérifiez que MySQL est démarré
- Vérifiez les identifiants dans `DBConnection.java`
- Vérifiez que la base de données `blog_jee` existe

### Erreur 404 sur les pages

- Vérifiez que le fichier WAR est bien déployé
- Vérifiez les mappings dans les annotations `@WebServlet`
- Vérifiez les chemins dans les JSP

### Problème d'envoi d'email

- Vérifiez les identifiants SMTP dans `EmailService.java`
- Pour Gmail, utilisez un "Mot de passe d'application"
- Vérifiez que le port 587 n'est pas bloqué par un firewall

## 📝 Notes

- Les Servlets utilisent des annotations `@WebServlet` pour le mapping des URLs
- Les filtres utilisent des annotations `@WebFilter`
- La pagination est configurée pour 10 articles par page
- Les sessions expirent après 30 minutes d'inactivité

## 👨‍💻 Développement

### Compiler et tester

```bash
# Compiler
mvn clean compile

# Créer le WAR
mvn clean package

# Exécuter les tests (si disponibles)
mvn test
```

### Structure MVC

- **Model** : Classes dans `models/` (POJO)
- **View** : Pages JSP dans `webapp/WEB-INF/views/`
- **Controller** : Servlets dans `controllers/`

## 📄 Licence

Ce projet est un projet éducatif développé pour l'apprentissage de Java EE.

## 🤝 Contribution

Ce projet est un exemple éducatif. N'hésitez pas à l'utiliser comme base pour vos propres projets.

---

**Auteur** : Projet Forum JEE  
**Version** : 1.0  
**Date** : 2024
