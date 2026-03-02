package com.example.forum_project.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filtre pour gérer la langue de l'interface (i18n)
 */
@WebFilter(filterName = "LangueFilter", urlPatterns = "/*")
public class LangueFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation si nécessaire
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        HttpSession session = httpRequest.getSession(true);
        
        // Vérifier le paramètre lang dans l'URL
        String lang = httpRequest.getParameter("lang");
        
        if (lang != null && (lang.equals("fr") || lang.equals("en"))) {
            // Définir la langue en session
            session.setAttribute("langue", lang);
        } else {
            // Si aucune langue n'est définie, utiliser la langue de la session ou français par défaut
            if (session.getAttribute("langue") == null) {
                session.setAttribute("langue", "fr");
            }
        }
        
        // Continuer la chaîne
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Nettoyage si nécessaire
    }
}
