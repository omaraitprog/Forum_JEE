<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:choose>
    <c:when test="${not empty sessionScope.locale}">
        <fmt:setLocale value="${sessionScope.locale}" />
    </c:when>
    <c:otherwise>
        <fmt:setLocale value="fr" />
    </c:otherwise>
</c:choose>
<fmt:setBundle basename="messages" />

<c:set var="pageTitle" value="<fmt:message key='nav.articles' />" scope="request" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />
<jsp:include page="/WEB-INF/views/includes/navbar.jsp" />

<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1><fmt:message key="article.liste" /></h1>
        <c:if test="${not empty sessionScope.utilisateur}">
            <a href="${pageContext.request.contextPath}/articles?action=create" class="btn btn-primary">
                <i class="fas fa-plus me-2"></i><fmt:message key="article.creer" />
            </a>
        </c:if>
    </div>
    
    <c:if test="${empty articles}">
        <div class="alert alert-info">
            <i class="fas fa-info-circle me-2"></i><fmt:message key="article.aucunArticle" />
        </div>
    </c:if>
    
    <div class="row">
        <c:forEach var="article" items="${articles}">
            <div class="col-md-4 mb-4">
                <div class="card h-100 shadow-sm">
                    <c:if test="${not empty article.imageUrl}">
                        <img src="${article.imageUrl}" class="card-img-top" alt="${article.titre}" style="height: 200px; object-fit: cover;">
                    </c:if>
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">${article.titre}</h5>
                        <p class="card-text text-muted flex-grow-1">
                            <c:choose>
                                <c:when test="${not empty article.resume}">
                                    ${article.resume}
                                </c:when>
                                <c:otherwise>
                                    ${fn:substring(article.contenu, 0, 150)}...
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <div class="mt-auto">
                            <small class="text-muted">
                                <i class="fas fa-user me-1"></i>
                                <fmt:message key="article.auteur" /> ${article.auteur.prenom} ${article.auteur.nom}
                            </small><br>
                            <small class="text-muted">
                                <i class="fas fa-calendar me-1"></i>
                                <fmt:message key="article.date" /> <fmt:formatDate value="${article.dateCreation}" pattern="dd/MM/yyyy HH:mm" />
                            </small>
                        </div>
                        <a href="${pageContext.request.contextPath}/articles?id=${article.id}" class="btn btn-outline-primary btn-sm mt-2">
                            <fmt:message key="article.lireSuite" /> <i class="fas fa-arrow-right ms-1"></i>
                        </a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    
    <!-- Pagination -->
    <c:if test="${totalPages > 1}">
        <nav aria-label="Pagination">
            <ul class="pagination justify-content-center">
                <li class="page-item ${page == 1 ? 'disabled' : ''}">
                    <a class="page-link" href="${pageContext.request.contextPath}/articles?page=${page - 1}">
                        <fmt:message key="pagination.precedent" />
                    </a>
                </li>
                <c:forEach var="i" begin="1" end="${totalPages}">
                    <li class="page-item ${page == i ? 'active' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/articles?page=${i}">${i}</a>
                    </li>
                </c:forEach>
                <li class="page-item ${page == totalPages ? 'disabled' : ''}">
                    <a class="page-link" href="${pageContext.request.contextPath}/articles?page=${page + 1}">
                        <fmt:message key="pagination.suivant" />
                    </a>
                </li>
            </ul>
        </nav>
    </c:if>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
