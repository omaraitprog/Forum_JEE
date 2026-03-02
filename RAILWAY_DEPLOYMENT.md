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

**IMPORTANT :** Pour forcer Railway à utiliser le Dockerfile au lieu de Nixpacks :

1. Dans votre projet Railway, allez dans les paramètres du service
2. Dans l'onglet "Settings" → "Build & Deploy"
3. Assurez-vous que "Builder" est défini sur **"Dockerfile"**
4. Le chemin du Dockerfile doit être `Dockerfile` (à la racine)

Si Railway essaie toujours d'utiliser Nixpacks :
- Le fichier `railway.json` devrait forcer l'utilisation du Dockerfile
- Le fichier `nixpacks.toml` désactive Nixpacks
- Vérifiez que le Dockerfile est bien à la racine du projet

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

### Erreur "JAVA_HOME is not defined correctly" ou "We cannot execute /usr/local/bin/java"

Cette erreur se produit lorsque Railway essaie d'utiliser Nixpacks (buildpack automatique) au lieu du Dockerfile.

**Solution :**

1. **Dans Railway, allez dans les paramètres de votre service :**
   - Cliquez sur votre service web
   - Allez dans "Settings" → "Build & Deploy"
   - Assurez-vous que "Builder" est défini sur **"Dockerfile"**
   - Le "Dockerfile Path" doit être `Dockerfile`

2. **Si le problème persiste :**
   - Supprimez le service et recréez-le
   - Lors de la création, sélectionnez explicitement "Dockerfile" comme builder
   - Ou utilisez la commande Railway CLI : `railway link` puis `railway up`

3. **Vérifiez que les fichiers suivants existent à la racine :**
   - `Dockerfile` (doit être présent)
   - `railway.json` (configuration Railway)
   - `nixpacks.toml` (désactive Nixpacks)

### L'application ne démarre pas

1. Vérifiez les logs dans Railway (onglet "Deployments" → "View Logs")
2. Vérifiez que toutes les variables d'environnement sont correctement configurées
3. Vérifiez que la base de données est accessible

### Erreur de connexion à la base de données

1. Vérifiez que les variables `DATABASE_URL`, `DB_USER`, et `DB_PASSWORD` sont correctes
2. Vérifiez que la base de données MySQL est bien démarrée sur Railway
3. Vérifiez que le script `schema.sql` a été exécuté

### Le WAR n'est pas trouvé

1. Le Dockerfile construit automatiquement le WAR avec Maven
2. Vérifiez les logs de build pour voir si `mvn clean package` s'est exécuté correctement
3. Vérifiez que le fichier WAR est bien copié depuis le stage de build vers Tomcat

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
