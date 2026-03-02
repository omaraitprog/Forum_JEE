package com.example.forum_project.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Service pour l'envoi d'emails
 * Configuration pour Gmail SMTP
 */
public class EmailService {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;
    private static final String SMTP_USER = "votre-email@gmail.com"; // À configurer
    private static final String SMTP_PASSWORD = "votre-mot-de-passe-app"; // À configurer (mot de passe d'application Gmail)
    
    private Session session;
    private boolean emailConfigured = false;
    
    /**
     * Constructeur qui initialise la session email
     */
    public EmailService() {
        // Vérifier si l'email est configuré (pas les valeurs par défaut)
        emailConfigured = !SMTP_USER.equals("votre-email@gmail.com") && 
                          !SMTP_PASSWORD.equals("votre-mot-de-passe-app") &&
                          !SMTP_PASSWORD.isEmpty();
        
        if (emailConfigured) {
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", SMTP_HOST);
            
            session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
                }
            });
        } else {
            System.out.println("ATTENTION: L'envoi d'email n'est pas configuré. Les utilisateurs devront être activés manuellement.");
        }
    }
    
    /**
     * Envoie un email de vérification à un utilisateur
     * @param email L'adresse email du destinataire
     * @param token Le token de vérification
     * @return true si l'email a été envoyé avec succès, false sinon
     */
    public boolean envoyerEmailVerification(String email, String token) {
        // Si l'email n'est pas configuré, retourner false sans erreur
        if (!emailConfigured) {
            System.out.println("Email non configuré. L'utilisateur peut être activé manuellement via le lien : " +
                             "http://localhost:8080/Forum_Project/verifier?token=" + token);
            return false;
        }
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Vérification de votre compte - Blog JEE");
            
            String verificationLink = "http://localhost:8080/Forum_Project/verifier?token=" + token;
            
            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<meta charset='UTF-8'>" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                    ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                    ".button { display: inline-block; padding: 12px 24px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }" +
                    ".button:hover { background-color: #0056b3; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<h2>Bienvenue sur notre Blog !</h2>" +
                    "<p>Merci de vous être inscrit. Pour activer votre compte, veuillez cliquer sur le lien ci-dessous :</p>" +
                    "<a href='" + verificationLink + "' class='button'>Vérifier mon compte</a>" +
                    "<p>Ou copiez ce lien dans votre navigateur :</p>" +
                    "<p style='word-break: break-all; color: #666;'>" + verificationLink + "</p>" +
                    "<p><strong>Ce lien est valable pendant 24 heures.</strong></p>" +
                    "<p>Si vous n'avez pas créé de compte, ignorez cet email.</p>" +
                    "<hr>" +
                    "<p style='color: #666; font-size: 12px;'>Cet email a été envoyé automatiquement, merci de ne pas y répondre.</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";
            
            message.setContent(htmlContent, "text/html; charset=UTF-8");
            
            Transport.send(message);
            System.out.println("Email de vérification envoyé avec succès à : " + email);
            return true;
        } catch (AuthenticationFailedException e) {
            System.err.println("ERREUR: Authentification email échouée. Vérifiez vos identifiants Gmail dans EmailService.java");
            System.err.println("Pour configurer Gmail:");
            System.err.println("1. Allez sur https://myaccount.google.com/apppasswords");
            System.err.println("2. Créez un mot de passe d'application");
            System.err.println("3. Mettez à jour SMTP_USER et SMTP_PASSWORD dans EmailService.java");
            return false;
        } catch (MessagingException e) {
            System.err.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
            // Ne pas afficher la stack trace complète pour les erreurs d'authentification
            if (!(e instanceof AuthenticationFailedException)) {
                e.printStackTrace();
            }
            return false;
        }
    }
    
    /**
     * Méthode pour configurer les identifiants SMTP (à appeler depuis un contexte ou fichier de configuration)
     * @param user Nom d'utilisateur SMTP
     * @param password Mot de passe SMTP
     */
    public static void configurerSMTP(String user, String password) {
        // Cette méthode peut être utilisée pour configurer dynamiquement les identifiants
        // Pour l'instant, ils sont définis comme constantes
    }
}
