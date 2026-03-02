package com.example.forum_project.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Locale;

@WebServlet("/language")
public class LanguageServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String lang = request.getParameter("lang");
        Locale locale;
        
        if ("en".equalsIgnoreCase(lang)) {
            locale = Locale.ENGLISH;
        } else {
            locale = Locale.FRENCH;
        }
        
        // Sauvegarder la locale dans la session
        request.getSession().setAttribute("locale", locale);
        
        // Rediriger vers la page précédente ou vers la page d'accueil
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect(request.getContextPath() + "/articles");
        }
    }
}
