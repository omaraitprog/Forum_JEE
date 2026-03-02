<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:choose>
    <c:when test="${not empty sessionScope.locale}">
        <fmt:setLocale value="${sessionScope.locale}" />
    </c:when>
    <c:otherwise>
        <fmt:setLocale value="fr" />
    </c:otherwise>
</c:choose>
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
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="languageDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fas fa-globe me-1"></i>
                        <fmt:message key="langue.changer" />
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="languageDropdown">
                        <li>
                            <a class="dropdown-item ${not empty sessionScope.locale && sessionScope.locale.language == 'fr' ? 'active' : ''}" 
                               href="${pageContext.request.contextPath}/language?lang=fr">
                                <i class="fas fa-flag me-2"></i><fmt:message key="langue.francais" />
                            </a>
                        </li>
                        <li>
                            <a class="dropdown-item ${not empty sessionScope.locale && sessionScope.locale.language == 'en' ? 'active' : ''}" 
                               href="${pageContext.request.contextPath}/language?lang=en">
                                <i class="fas fa-flag me-2"></i><fmt:message key="langue.anglais" />
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
                                        <i class="fas fa-plus me-2"></i><fmt:message key="article.nouveau" />
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
