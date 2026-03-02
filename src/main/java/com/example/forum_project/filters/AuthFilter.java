package com.example.forum_project.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filtre pour protéger les pages nécessitant une authentification
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/profil/*"})
public class AuthFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation si nécessaire
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        HttpSession session = httpRequest.getSession(false);
        
        // Vérifier si l'utilisateur est connecté
        if (session == null || session.getAttribute("utilisateur") == null) {
            // Rediriger vers la page de connexion
            String contextPath = httpRequest.getContextPath();
            httpResponse.sendRedirect(contextPath + "/login");
            return;
        }
        
        // L'utilisateur est connecté, continuer la chaîne
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Nettoyage si nécessaire
    }
}
