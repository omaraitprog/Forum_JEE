# Guide de déploiement sur Railway

Ce guide vous explique comment déployer votre application Forum JEE sur Railway.

## 📋 Prérequis

1. Un compte GitHub avec le code poussé
2. Un compte Railway (gratuit disponible sur [railway.app](https://railway.app))
3. Une base de données MySQL (Railway propose MySQL comme service)

## 🚀 Étapes de déploiement

### 1. Préparer le projet localement

Assurez-vous que votre projet compile correctement :

```bash
mvn clean package
```

Cela génère le fichier WAR dans `target/Forum_Project-1.0-SNAPSHOT.war`.

### 2. Créer un projet sur Railway

1. Connectez-vous à [railway.app](https://railway.app)
2. Cliquez sur "New Project"
3. Sélectionnez "Deploy from GitHub repo"
4. Choisissez votre dépôt `Forum_JEE`

### 3. Ajouter une base de données MySQL

1. Dans votre projet Railway, cliquez sur "New"
2. Sélectionnez "Database" → "MySQL"
3. Railway créera automatiquement une base de données MySQL

### 4. Configurer les variables d'environnement

Dans les paramètres de votre service web sur Railway, ajoutez les variables d'environnement suivantes :

#### Variables de base de données

Railway fournit automatiquement une variable `MYSQL_URL` pour les services MySQL. Vous devez extraire les informations de connexion :

1. Allez dans votre service MySQL sur Railway
2. Cliquez sur l'onglet "Variables"
3. Copiez les valeurs suivantes :

```
DATABASE_URL=jdbc:mysql://[HOST]:[PORT]/[DATABASE]?useSSL=true&serverTimezone=UTC&characterEncoding=UTF-8
DB_USER=[USER]
DB_PASSWORD=[PASSWORD]
```

**Exemple :**
```
DATABASE_URL=jdbc:mysql://containers-us-west-xxx.railway.app:3306/railway?useSSL=true&serverTimezone=UTC&characterEncoding=UTF-8
DB_USER=root
DB_PASSWORD=your_password_here
```

#### Variables d'email (optionnel)

Si vous utilisez l'envoi d'emails, ajoutez également :

```
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USER=votre-email@gmail.com
SMTP_PASSWORD=votre-mot-de-passe-app
```

### 5. Configurer le build

Railway détectera automatiquement le Dockerfile et construira l'image Docker.

**Important :** Assurez-vous que le build Maven s'exécute avant le build Docker. Railway exécutera automatiquement `mvn clean package` si un `pom.xml` est détecté.

### 6. Initialiser la base de données

Une fois l'application déployée, vous devez initialiser la base de données :

1. Connectez-vous à votre base de données MySQL sur Railway
2. Utilisez l'onglet "Query" ou connectez-vous via un client MySQL
3. Exécutez le script `schema.sql` pour créer les tables

**Alternative :** Vous pouvez utiliser l'interface MySQL de Railway pour exécuter le script SQL.

### 7. Déployer

1. Railway détectera automatiquement les changements sur la branche `main`
2. Le déploiement commencera automatiquement
3. Une fois terminé, Railway vous fournira une URL publique (ex: `https://your-app.railway.app`)

## 🔧 Configuration avancée

### Port personnalisé

Railway utilise automatiquement le port défini dans la variable d'environnement `PORT`. Tomcat écoute sur le port 8080 par défaut, mais Railway peut mapper cela automatiquement.

### Build personnalisé

Si vous avez besoin d'un build personnalisé, vous pouvez créer un script `build.sh` :

```bash
#!/bin/bash
mvn clean package
```

Et l'ajouter dans `railway.json` :

```json
{
  "build": {
    "builder": "DOCKERFILE",
    "dockerfilePath": "Dockerfile",
    "buildCommand": "./build.sh"
  }
}
```

## 🐛 Dépannage

### L'application ne démarre pas

1. Vérifiez les logs dans Railway (onglet "Deployments" → "View Logs")
2. Vérifiez que toutes les variables d'environnement sont correctement configurées
3. Vérifiez que la base de données est accessible

### Erreur de connexion à la base de données

1. Vérifiez que les variables `DATABASE_URL`, `DB_USER`, et `DB_PASSWORD` sont correctes
2. Vérifiez que la base de données MySQL est bien démarrée sur Railway
3. Vérifiez que le script `schema.sql` a été exécuté

### Le WAR n'est pas trouvé

1. Assurez-vous que `mvn clean package` s'exécute avant le build Docker
2. Vérifiez que le fichier WAR est bien généré dans `target/`

## 📝 Notes importantes

- Railway redéploie automatiquement à chaque push sur la branche `main`
- Les variables d'environnement peuvent être modifiées sans redéployer
- Railway fournit un domaine HTTPS gratuit
- La base de données MySQL sur Railway est persistante

## 🔗 Ressources

- [Documentation Railway](https://docs.railway.app)
- [Railway Discord](https://discord.gg/railway)
- [Exemples de déploiement Railway](https://docs.railway.app/examples)

---

**Bon déploiement ! 🚀**
