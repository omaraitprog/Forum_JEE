package com.example.forum_project.services;

import com.example.forum_project.dao.ArticleDAO;
import com.example.forum_project.models.Article;
import com.example.forum_project.utils.WebScraper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service pour importer les cours depuis mazoul.online comme articles
 */
public class ImportArticleService {
    
    private WebScraper webScraper;
    private ArticleDAO articleDAO;
    
    public ImportArticleService() {
        this.webScraper = new WebScraper();
        this.articleDAO = new ArticleDAO();
    }
    
    /**
     * Importe tous les cours depuis mazoul.online
     * @param auteurId ID de l'auteur qui sera assigné aux articles importés
     * @return Liste des IDs des articles créés
     */
    public List<Integer> importerTousLesCours(int auteurId) {
        List<Integer> articlesCrees = new ArrayList<>();
        
        try {
            // Récupérer tous les cours depuis le site
            List<Map<String, String>> cours = webScraper.recupererCours();
            
            System.out.println("Nombre de cours trouvés: " + cours.size());
            
            for (Map<String, String> coursData : cours) {
                try {
                    Article article = creerArticleDepuisCours(coursData, auteurId);
                    
                    // Vérifier si l'article existe déjà (par titre)
                    if (!articleExiste(article.getTitre())) {
                        int articleId = articleDAO.create(article);
                        if (articleId > 0) {
                            articlesCrees.add(articleId);
                            System.out.println("Article importé: " + article.getTitre());
                        }
                    } else {
                        System.out.println("Article déjà existant, ignoré: " + article.getTitre());
                    }
                } catch (Exception e) {
                    System.err.println("Erreur lors de l'importation d'un cours: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'importation des cours: " + e.getMessage());
            e.printStackTrace();
        }
        
        return articlesCrees;
    }
    
    /**
     * Importe les cours depuis une URL spécifique
     * @param url URL de la page contenant les cours
     * @param auteurId ID de l'auteur
     * @return Liste des IDs des articles créés
     */
    public List<Integer> importerCoursDepuisUrl(String url, int auteurId) {
        List<Integer> articlesCrees = new ArrayList<>();
        
        try {
            List<Map<String, String>> cours = webScraper.recupererCoursDepuisUrl(url);
            
            for (Map<String, String> coursData : cours) {
                try {
                    Article article = creerArticleDepuisCours(coursData, auteurId);
                    
                    if (!articleExiste(article.getTitre())) {
                        int articleId = articleDAO.create(article);
                        if (articleId > 0) {
                            articlesCrees.add(articleId);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Erreur lors de l'importation d'un cours: " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'importation depuis l'URL: " + e.getMessage());
            e.printStackTrace();
        }
        
        return articlesCrees;
    }
    
    /**
     * Crée un objet Article à partir des données d'un cours
     */
    private Article creerArticleDepuisCours(Map<String, String> coursData, int auteurId) {
        Article article = new Article();
        
        article.setTitre(coursData.get("titre"));
        article.setContenu(coursData.get("contenu"));
        article.setResume(coursData.get("resume"));
        article.setAuteurId(auteurId);
        article.setStatut("PUBLIE");
        
        // Optionnel: extraire l'URL de l'image si disponible
        if (coursData.containsKey("image_url")) {
            article.setImageUrl(coursData.get("image_url"));
        }
        
        return article;
    }
    
    /**
     * Vérifie si un article avec ce titre existe déjà
     */
    private boolean articleExiste(String titre) {
        // Recherche simple par titre dans la base de données
        // Pour une vérification plus robuste, on pourrait améliorer ArticleDAO
        try {
            // On pourrait ajouter une méthode findByTitle dans ArticleDAO
            // Pour l'instant, on retourne false pour permettre l'importation
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Nettoie le contenu HTML pour le rendre compatible avec l'affichage
     */
    public String nettoyerContenu(String contenuHtml) {
        if (contenuHtml == null) return "";
        
        // Nettoyer les balises potentiellement dangereuses
        contenuHtml = contenuHtml.replaceAll("<script[^>]*>.*?</script>", "");
        contenuHtml = contenuHtml.replaceAll("<style[^>]*>.*?</style>", "");
        
        return contenuHtml;
    }
}
