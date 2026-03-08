# Guide de déploiement sur Railway avec SQLite

Ce guide vous explique comment déployer votre application Forum JEE sur Railway avec SQLite.

## 📋 Prérequis

1. Un compte GitHub avec le code poussé
2. Un compte Railway (gratuit disponible sur [railway.app](https://railway.app))
3. **Note importante**: SQLite est une base de données fichier, adaptée pour des applications à faible trafic

## ⚠️ Important: Persistance des données SQLite sur Railway

Railway utilise un système de fichiers **éphémère** par défaut. Pour que SQLite persiste les données:

### Option 1: Utiliser le répertoire `/tmp` (Recommandé pour débuter)
- Le répertoire `/tmp` sur Railway persiste entre les redéploiements
- La base de données sera automatiquement créée dans `/tmp/blog_jee.db`
- **Limitation**: Les données peuvent être perdues lors d'un redéploiement complet

### Option 2: Utiliser Railway Volumes (Recommandé pour production)
- Créez un volume Railway pour une persistance garantie
- Configurez `DATABASE_PATH` pour pointer vers le volume

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
4. Choisissez votre dépôt `Forum_Project`

### 3. Configurer les variables d'environnement

Dans les paramètres de votre service web sur Railway, ajoutez les variables d'environnement suivantes :

#### Variables de base de données SQLite

**Option A: Utiliser `/tmp` (par défaut)**
```
DATABASE_PATH=/tmp/blog_jee.db
```

**Option B: Utiliser un volume Railway (recommandé pour production)**

1. Créez un volume Railway :
   - Dans votre projet Railway, cliquez sur "New" → "Volume"
   - Nommez-le `database-volume`
   - Notez le chemin du volume (ex: `/data`)

2. Configurez la variable :
   ```
   DATABASE_PATH=/data/blog_jee.db
   ```

#### Variables d'email (optionnel)

Si vous utilisez l'envoi d'emails, ajoutez également :

```
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USER=votre-email@gmail.com
SMTP_PASSWORD=votre-mot-de-passe-app
```

### 4. Configurer le build

**IMPORTANT :** Pour forcer Railway à utiliser le Dockerfile :

1. Dans votre projet Railway, allez dans les paramètres du service
2. Dans l'onglet "Settings" → "Build & Deploy"
3. Assurez-vous que "Builder" est défini sur **"Dockerfile"**
4. Le chemin du Dockerfile doit être `Dockerfile` (à la racine)

Le fichier `railway.json` devrait déjà forcer l'utilisation du Dockerfile.

### 5. Initialisation automatique de la base de données

✅ **Bonne nouvelle !** L'application initialise automatiquement la base de données SQLite au démarrage :
- Les tables sont créées automatiquement si elles n'existent pas
- Aucune action manuelle requise

Si vous voulez ajouter des données initiales, vous pouvez :
- Utiliser l'interface web de l'application
- Ou créer un script d'initialisation personnalisé

### 6. Déployer

1. Railway détectera automatiquement les changements sur la branche `main`
2. Le déploiement commencera automatiquement
3. Une fois terminé, Railway vous fournira une URL publique (ex: `https://your-app.railway.app`)

## 🔧 Configuration avancée

### Utiliser un volume Railway pour la persistance

Pour une persistance garantie des données :

1. **Créer un volume** :
   - Dans Railway, cliquez sur "New" → "Volume"
   - Nommez-le (ex: `database-volume`)
   - Notez le chemin (généralement `/data` ou `/volumes/database-volume`)

2. **Configurer la variable d'environnement** :
   ```
   DATABASE_PATH=/data/blog_jee.db
   ```
   (Remplacez `/data` par le chemin de votre volume)

3. **Attacher le volume au service** :
   - Dans les paramètres du service web
   - Onglet "Settings" → "Volumes"
   - Attachez le volume créé

### Port personnalisé

Railway utilise automatiquement le port défini dans la variable d'environnement `PORT`. Tomcat écoute sur le port 8080 par défaut, mais Railway peut mapper cela automatiquement.

## 🐛 Dépannage

### Erreur "JAVA_HOME is not defined correctly"

Cette erreur se produit lorsque Railway essaie d'utiliser Nixpacks au lieu du Dockerfile.

**Solution :**
1. Dans Railway, allez dans les paramètres de votre service
2. "Settings" → "Build & Deploy"
3. Assurez-vous que "Builder" est défini sur **"Dockerfile"**

### L'application ne démarre pas

1. Vérifiez les logs dans Railway (onglet "Deployments" → "View Logs")
2. Vérifiez que la variable `DATABASE_PATH` est correctement configurée
3. Vérifiez que le répertoire de la base de données est accessible en écriture

### Erreur de permissions sur la base de données

Si vous obtenez des erreurs de permissions :

1. Vérifiez que le chemin `DATABASE_PATH` est accessible en écriture
2. Sur Railway, utilisez `/tmp` ou un volume Railway
3. Vérifiez les logs pour voir le chemin exact utilisé

### Les données sont perdues après un redéploiement

**Cause**: La base de données est dans un répertoire éphémère.

**Solution**: 
- Utilisez un **Railway Volume** pour une persistance garantie
- Ou utilisez `/tmp` qui persiste entre les redéploiements (mais peut être perdu lors d'un redéploiement complet)

### Base de données verrouillée

SQLite peut verrouiller la base de données si plusieurs processus tentent d'y accéder simultanément. Sur Railway avec une seule instance, cela ne devrait pas être un problème.

## 📝 Notes importantes

- ✅ Railway redéploie automatiquement à chaque push sur la branche `main`
- ✅ Les variables d'environnement peuvent être modifiées sans redéployer
- ✅ Railway fournit un domaine HTTPS gratuit
- ⚠️ SQLite est adapté pour des applications à faible/moyen trafic
- ⚠️ Pour un trafic élevé, considérez PostgreSQL ou MySQL sur Railway
- ✅ La base de données est initialisée automatiquement au premier démarrage

## 🔄 Migration depuis MySQL

Si vous migrez depuis MySQL sur Railway :

1. **Exportez vos données MySQL** :
   ```sql
   -- Via l'interface Railway MySQL ou un client MySQL
   ```

2. **Convertissez le format SQL** pour SQLite (si nécessaire)

3. **Importez dans SQLite** via l'interface web de l'application ou un script

## 🔗 Ressources

- [Documentation Railway](https://docs.railway.app)
- [Railway Volumes](https://docs.railway.app/volumes)
- [Railway Discord](https://discord.gg/railway)
- [SQLite Documentation](https://www.sqlite.org/docs.html)

---

**Bon déploiement ! 🚀**
