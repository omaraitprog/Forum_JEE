<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.langue}" />
<fmt:setBundle basename="messages" />

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/articles">
            <i class="fas fa-blog me-2"></i>Blog JEE
        </a>
        
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/articles">
                        <fmt:message key="nav.accueil" />
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/articles">
                        <fmt:message key="nav.articles" />
                    </a>
                </li>
            </ul>
            
            <ul class="navbar-nav">
                <!-- Sélecteur de langue -->
                <li class="nav-item dropdown me-3">
                    <a class="nav-link dropdown-toggle" href="#" id="langDropdown" role="button" data-bs-toggle="dropdown">
                        <i class="fas fa-globe me-1"></i><fmt:message key="langue.label" />
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end">
                        <li>
                            <a class="dropdown-item" href="?lang=fr">
                                <fmt:message key="langue.francais" />
                            </a>
                        </li>
                        <li>
                            <a class="dropdown-item" href="?lang=en">
                                <fmt:message key="langue.anglais" />
                            </a>
                        </li>
                    </ul>
                </li>
                
                <c:choose>
                    <c:when test="${not empty sessionScope.utilisateur}">
                        <!-- Utilisateur connecté -->
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                                <c:if test="${not empty sessionScope.utilisateur.photoProfil}">
                                    <img src="${sessionScope.utilisateur.photoProfil}" alt="Photo" class="rounded-circle me-1" width="25" height="25">
                                </c:if>
                                <i class="fas fa-user me-1"></i>
                                ${sessionScope.utilisateur.prenom} ${sessionScope.utilisateur.nom}
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/profil">
                                        <i class="fas fa-user-circle me-2"></i><fmt:message key="nav.profil" />
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/articles?action=create">
                                        <i class="fas fa-plus me-2"></i><fmt:message key="article.creer" />
                                    </a>
                                </li>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/logout">
                                        <i class="fas fa-sign-out-alt me-2"></i><fmt:message key="nav.deconnexion" />
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <!-- Utilisateur non connecté -->
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/login">
                                <fmt:message key="nav.connexion" />
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/inscription">
                                <fmt:message key="nav.inscription" />
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

<script>
// Fonction pour préserver les paramètres de l'URL lors du changement de langue
document.querySelectorAll('#langDropdown + ul a').forEach(function(link) {
    link.addEventListener('click', function(e) {
        e.preventDefault();
        const url = new URL(window.location.href);
        url.searchParams.set('lang', this.href.includes('lang=fr') ? 'fr' : 'en');
        window.location.href = url.toString();
    });
});
</script>
