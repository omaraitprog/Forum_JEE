package com.example.forum_project.controllers;

import com.example.forum_project.utils.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Servlet de vérification de santé pour Railway
 * Répond à /health pour indiquer que l'application est en cours d'exécution
 * Répond à /health/db pour vérifier la connexion à la base de données
 */
@WebServlet("/health")
public class HealthCheckServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String testDb = request.getParameter("db");
        
        if ("true".equals(testDb)) {
            // Endpoint de test de la base de données
            testDatabase(response);
        } else {
            // Endpoint de santé standard
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"status\":\"ok\",\"service\":\"Forum Project\"}");
        }
    }
    
    private void testDatabase(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            String dbUrl = metaData.getURL();
            
            // Vérifier si la table utilisateurs existe
            boolean tableExists = false;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                     "SELECT name FROM sqlite_master WHERE type='table' AND name='utilisateurs'")) {
                tableExists = rs.next();
            }
            
            // Compter les utilisateurs
            int userCount = 0;
            if (tableExists) {
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM utilisateurs")) {
                    if (rs.next()) {
                        userCount = rs.getInt(1);
                    }
                }
            }
            
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(String.format(
                "{\"status\":\"ok\",\"database\":\"connected\",\"url\":\"%s\",\"table_exists\":%s,\"user_count\":%d}",
                dbUrl, tableExists, userCount
            ));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(String.format(
                "{\"status\":\"error\",\"database\":\"failed\",\"error\":\"%s\"}",
                e.getMessage().replace("\"", "\\\"")
            ));
        }
    }
}
