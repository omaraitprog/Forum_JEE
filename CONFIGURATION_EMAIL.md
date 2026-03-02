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

### Étape 3 : Configurer dans le code

Ouvrez le fichier : `src/main/java/com/example/forum_project/utils/EmailService.java`

Modifiez les lignes 15-16 :

```java
private static final String SMTP_USER = "votre-email@gmail.com"; // Remplacez par votre email Gmail
private static final String SMTP_PASSWORD = "votre-mot-de-passe-app"; // Collez le mot de passe d'application ici
```

**Exemple :**
```java
private static final String SMTP_USER = "monblog@gmail.com";
private static final String SMTP_PASSWORD = "abcd efgh ijkl mnop"; // Le mot de passe d'application (16 caractères)
```

### Étape 4 : Redémarrer l'application

Après avoir modifié le code, redémarrez Tomcat pour que les changements prennent effet.

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

Si vous ne voulez pas utiliser Gmail, vous pouvez modifier les paramètres dans `EmailService.java` :

**Outlook/Hotmail :**
```java
private static final String SMTP_HOST = "smtp-mail.outlook.com";
private static final int SMTP_PORT = 587;
```

**Yahoo :**
```java
private static final String SMTP_HOST = "smtp.mail.yahoo.com";
private static final int SMTP_PORT = 587;
```

**Serveur SMTP personnalisé :**
Modifiez `SMTP_HOST` et `SMTP_PORT` selon votre configuration.

---

**Note :** L'application fonctionne parfaitement sans configuration email. Les utilisateurs peuvent activer leur compte manuellement via le lien affiché après l'inscription.
