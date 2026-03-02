package com.example.forum_project.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Locale;

@WebFilter(filterName = "LocaleFilter", urlPatterns = "/*")
public class LocaleFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation si nécessaire
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        
        // Récupérer la locale depuis la session, ou utiliser le français par défaut
        Locale locale = (Locale) session.getAttribute("locale");
        if (locale == null) {
            // Par défaut, utiliser le français
            locale = Locale.FRENCH;
            session.setAttribute("locale", locale);
        }
        
        // Définir la locale pour la requête
        httpRequest.setAttribute("locale", locale);
        
        // Passer au filtre suivant
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Nettoyage si nécessaire
    }
}
