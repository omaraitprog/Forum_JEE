package com.example.forum_project.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilitaire pour récupérer le contenu depuis mazoul.online
 * Scrape les cours/articles depuis le site web
 */
public class WebScraper {
    
    private static final String BASE_URL = "https://mazoul.online";
    private static final int TIMEOUT = 10000; // 10 secondes
    
    /**
     * Récupère tous les cours/articles depuis mazoul.online
     * @return Liste des articles avec titre, contenu et résumé
     */
    public List<Map<String, String>> recupererCours() {
        List<Map<String, String>> articles = new ArrayList<>();
        
        try {
            // Connexion au site principal
            Document doc = Jsoup.connect(BASE_URL)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(TIMEOUT)
                    .get();
            
            // Recherche des liens vers les cours/articles
            // Adaptez ces sélecteurs selon la structure réelle du site mazoul.online
            Elements links = doc.select("a[href*=/cours/], a[href*=/article/], a[href*=/post/], article a, .course-link, .article-link");
            
            for (Element link : links) {
                String href = link.attr("href");
                String absoluteUrl = href.startsWith("http") ? href : BASE_URL + href;
                
                try {
                    Map<String, String> article = recupererArticle(absoluteUrl);
                    if (article != null && !article.isEmpty()) {
                        articles.add(article);
                    }
                } catch (Exception e) {
                    System.err.println("Erreur lors de la récupération de l'article: " + absoluteUrl);
                    e.printStackTrace();
                }
            }
            
            // Si aucun lien trouvé, essayons de récupérer directement les articles de la page principale
            if (articles.isEmpty()) {
                articles = recupererArticlesDepuisPage(doc);
            }
            
        } catch (IOException e) {
            System.err.println("Erreur lors de la connexion à mazoul.online: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur générale lors du scraping: " + e.getMessage());
            e.printStackTrace();
        }
        
        return articles;
    }
    
    /**
     * Récupère un article spécifique depuis son URL
     */
    public Map<String, String> recupererArticle(String url) throws IOException {
        Map<String, String> article = new HashMap<>();
        
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .timeout(TIMEOUT)
                .get();
        
        // Extraction du titre - essayons plusieurs sélecteurs possibles
        String titre = null;
        String[] titreSelectors = {
            "h1", "h1.title", "h1.article-title", ".post-title", 
            ".article-header h1", "title", "h2.title"
        };
        
        for (String selector : titreSelectors) {
            Element titleElement = doc.selectFirst(selector);
            if (titleElement != null) {
                titre = titleElement.text().trim();
                if (!titre.isEmpty()) break;
            }
        }
        
        if (titre == null || titre.isEmpty()) {
            titre = doc.title();
        }
        
        // Extraction du contenu - essayons plusieurs sélecteurs possibles
        String contenu = null;
        String[] contenuSelectors = {
            "article", ".article-content", ".post-content", ".content",
            ".entry-content", "main", ".main-content", "#content"
        };
        
        for (String selector : contenuSelectors) {
            Element contentElement = doc.selectFirst(selector);
            if (contentElement != null) {
                // Nettoyer le HTML et garder le formatage de base
                contenu = contentElement.html();
                if (!contenu.trim().isEmpty()) break;
            }
        }
        
        // Si aucun contenu trouvé, prendre le body
        if (contenu == null || contenu.trim().isEmpty()) {
            Element body = doc.body();
            if (body != null) {
                // Retirer les scripts et styles
                body.select("script, style, nav, header, footer, aside").remove();
                contenu = body.html();
            }
        }
        
        // Extraction du résumé - essayons plusieurs sélecteurs possibles
        String resume = null;
        String[] resumeSelectors = {
            "meta[property=og:description]", "meta[name=description]",
            ".excerpt", ".summary", ".article-summary", ".post-excerpt"
        };
        
        for (String selector : resumeSelectors) {
            Element resumeElement = doc.selectFirst(selector);
            if (resumeElement != null) {
                resume = resumeElement.hasAttr("content") 
                    ? resumeElement.attr("content") 
                    : resumeElement.text();
                if (resume != null && !resume.trim().isEmpty()) break;
            }
        }
        
        // Si pas de résumé, créer un résumé à partir du contenu
        if (resume == null || resume.isEmpty()) {
            String texteBrut = doc.text();
            if (texteBrut.length() > 200) {
                resume = texteBrut.substring(0, 200) + "...";
            } else {
                resume = texteBrut;
            }
        }
        
        // Limiter la taille du résumé
        if (resume.length() > 500) {
            resume = resume.substring(0, 500) + "...";
        }
        
        if (titre != null && contenu != null && !titre.isEmpty() && !contenu.trim().isEmpty()) {
            article.put("titre", titre);
            article.put("contenu", contenu);
            article.put("resume", resume != null ? resume : "");
            article.put("url", url);
        }
        
        return article;
    }
    
    /**
     * Récupère les articles directement depuis la page principale
     */
    private List<Map<String, String>> recupererArticlesDepuisPage(Document doc) {
        List<Map<String, String>> articles = new ArrayList<>();
        
        // Recherche des articles sur la page principale
        Elements articleElements = doc.select("article, .article, .post, .course, .lesson");
        
        for (Element articleElement : articleElements) {
            Map<String, String> article = new HashMap<>();
            
            // Titre
            Element titleElement = articleElement.selectFirst("h1, h2, h3, .title, .article-title");
            String titre = titleElement != null ? titleElement.text().trim() : "";
            
            // Contenu
            Element contentElement = articleElement.selectFirst(".content, .article-content, .post-content, p");
            String contenu = contentElement != null ? contentElement.html() : articleElement.html();
            
            // Résumé
            Element resumeElement = articleElement.selectFirst(".excerpt, .summary, .description");
            String resume = resumeElement != null ? resumeElement.text().trim() : "";
            
            if (!titre.isEmpty() && !contenu.trim().isEmpty()) {
                article.put("titre", titre);
                article.put("contenu", contenu);
                article.put("resume", resume.length() > 500 ? resume.substring(0, 500) + "..." : resume);
                articles.add(article);
            }
        }
        
        return articles;
    }
    
    /**
     * Récupère les cours depuis une URL spécifique
     */
    public List<Map<String, String>> recupererCoursDepuisUrl(String url) {
        List<Map<String, String>> articles = new ArrayList<>();
        
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(TIMEOUT)
                    .get();
            
            // Recherche des liens vers les cours
            Elements links = doc.select("a[href*=/cours/], a[href*=/article/], a[href*=/post/]");
            
            for (Element link : links) {
                String href = link.attr("href");
                String absoluteUrl = href.startsWith("http") ? href : BASE_URL + href;
                
                try {
                    Map<String, String> article = recupererArticle(absoluteUrl);
                    if (article != null && !article.isEmpty()) {
                        articles.add(article);
                    }
                } catch (Exception e) {
                    System.err.println("Erreur lors de la récupération de l'article: " + absoluteUrl);
                }
            }
            
        } catch (IOException e) {
            System.err.println("Erreur lors de la connexion à l'URL: " + url);
            e.printStackTrace();
        }
        
        return articles;
    }
}
