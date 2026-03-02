# Configuration de l'envoi d'emails (Optionnel)

## ⚠️ Important

L'application fonctionne **sans configuration email**. Si l'email n'est pas configuré :
- Les utilisateurs peuvent toujours s'inscrire
- Un lien de vérification sera affiché directement après l'inscription
- Les utilisateurs peuvent activer leur compte en cliquant sur ce lien

## Configuration Gmail (Optionnel)

Si vous souhaitez activer l'envoi automatique d'emails de vérification :

### Étape 1 : Activer la validation en 2 étapes

1. Allez sur https://myaccount.google.com/security
2. Activez la "Validation en deux étapes" si ce n'est pas déjà fait

### Étape 2 : Créer un mot de passe d'application

1. Allez sur https://myaccount.google.com/apppasswords
2. Sélectionnez "Application" : **Mail**
3. Sélectionnez "Appareil" : **Autre (nom personnalisé)**
4. Entrez un nom (ex: "Blog JEE")
5. Cliquez sur "Générer"
6. **Copiez le mot de passe généré** (16 caractères sans espaces)

### Étape 3 : Configurer les variables d'environnement (Recommandé)

L'application utilise des variables d'environnement pour la configuration email. C'est la méthode recommandée car elle évite de stocker des identifiants dans le code.

#### Option A : Configuration dans IntelliJ IDEA (Recommandé pour le développement)

1. Dans IntelliJ IDEA, allez dans **Run** → **Edit Configurations...**
2. Sélectionnez votre configuration Tomcat
3. Dans l'onglet **Environment variables**, ajoutez :
   ```
   SMTP_USER=votre-email@gmail.com
   SMTP_PASSWORD=votre-mot-de-passe-app
   APP_BASE_URL=http://localhost:8081/Forum_Project
   ```
   (Remplacez `8081` par le port que vous utilisez si différent)

4. Cliquez sur **OK** et redémarrez l'application

#### Option B : Configuration via variables d'environnement système (Windows)

1. Ouvrez les **Paramètres système** → **Variables d'environnement**
2. Créez les variables suivantes :
   - `SMTP_USER` = `votre-email@gmail.com`
   - `SMTP_PASSWORD` = `votre-mot-de-passe-app`
   - `APP_BASE_URL` = `http://localhost:8081/Forum_Project`

3. Redémarrez IntelliJ IDEA et Tomcat

#### Option C : Configuration directe dans le code (Alternative)

Si vous préférez configurer directement dans le code, ouvrez le fichier : `src/main/java/com/example/forum_project/utils/EmailService.java`

Modifiez les lignes 20-25 pour remplacer les valeurs par défaut :

```java
private static final String SMTP_USER = "monblog@gmail.com";
private static final String SMTP_PASSWORD = "abcd efgh ijkl mnop"; // Le mot de passe d'application (16 caractères)
```

**⚠️ Attention :** Cette méthode n'est pas recommandée pour la production car elle expose les identifiants dans le code source.

### Étape 4 : Redémarrer l'application

Après avoir configuré les variables d'environnement ou modifié le code, redémarrez Tomcat pour que les changements prennent effet.

## Test

1. Inscrivez un nouvel utilisateur
2. Vérifiez votre boîte email (et les spams)
3. Cliquez sur le lien de vérification dans l'email

## Dépannage

### Erreur : "Username and Password not accepted"

- Vérifiez que vous utilisez un **mot de passe d'application** (pas votre mot de passe Gmail normal)
- Vérifiez que la validation en 2 étapes est activée
- Vérifiez que le mot de passe d'application est correct (16 caractères)

### Erreur : "Connection timeout"

- Vérifiez votre connexion Internet
- Vérifiez que le port 587 n'est pas bloqué par un firewall

### L'email n'est pas reçu

- Vérifiez votre dossier spam
- Vérifiez que l'adresse email de destination est correcte
- Vérifiez les logs de Tomcat pour voir les erreurs

## Alternative : Utiliser un autre service SMTP

Si vous ne voulez pas utiliser Gmail, vous pouvez configurer un autre service SMTP via les variables d'environnement :

**Outlook/Hotmail :**
```
SMTP_HOST=smtp-mail.outlook.com
SMTP_PORT=587
SMTP_USER=votre-email@outlook.com
SMTP_PASSWORD=votre-mot-de-passe-app
```

**Yahoo :**
```
SMTP_HOST=smtp.mail.yahoo.com
SMTP_PORT=587
SMTP_USER=votre-email@yahoo.com
SMTP_PASSWORD=votre-mot-de-passe-app
```

**Serveur SMTP personnalisé :**
Configurez `SMTP_HOST` et `SMTP_PORT` selon votre configuration via les variables d'environnement.

**Note :** Vous pouvez également modifier directement les valeurs par défaut dans `EmailService.java` (lignes 14-19), mais l'utilisation de variables d'environnement est préférable.

---

**Note :** L'application fonctionne parfaitement sans configuration email. Les utilisateurs peuvent activer leur compte manuellement via le lien affiché après l'inscription.
