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

<c:set var="pageTitle" value="${article.titre}" scope="request" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />
<jsp:include page="/WEB-INF/views/includes/navbar.jsp" />

<div class="container mt-4">
    <div class="row">
        <div class="col-lg-8">
            <!-- Article -->
            <article class="card shadow-sm mb-4">
                <c:if test="${not empty article.imageUrl}">
                    <img src="${article.imageUrl}" class="card-img-top" alt="${article.titre}">
                </c:if>
                <div class="card-body">
                    <h1 class="card-title">${article.titre}</h1>
                    <div class="text-muted mb-3">
                        <i class="fas fa-user me-1"></i>
                        <fmt:message key="article.auteur" /> ${article.auteur.prenom} ${article.auteur.nom}
                        <span class="ms-3">
                            <i class="fas fa-calendar me-1"></i>
                            <fmt:message key="article.publieLe" /> <fmt:formatDate value="${article.dateCreation}" pattern="dd/MM/yyyy à HH:mm" />
                        </span>
                    </div>
                    <div class="card-text" style="white-space: pre-wrap;">
                        <c:out value="${article.contenu}" />
                    </div>
                    
                    <!-- Actions (si auteur ou admin) -->
                    <c:if test="${not empty sessionScope.utilisateur && 
                                  (sessionScope.utilisateur.id == article.auteurId || sessionScope.utilisateur.role == 'ADMIN')}">
                        <hr>
                        <div class="btn-group">
                            <a href="${pageContext.request.contextPath}/articles?action=edit&id=${article.id}" 
                               class="btn btn-sm btn-outline-primary">
                                <i class="fas fa-edit me-1"></i><fmt:message key="article.modifier" />
                            </a>
                            <c:set var="confirmDelete"><fmt:message key="article.confirmerSuppression" /></c:set>
                            <form method="post" action="${pageContext.request.contextPath}/articles" 
                                  style="display: inline;" 
                                  onsubmit="return confirm('${confirmDelete}');">
                                <input type="hidden" name="_method" value="DELETE">
                                <input type="hidden" name="id" value="${article.id}">
                                <button type="submit" class="btn btn-sm btn-outline-danger">
                                    <i class="fas fa-trash me-1"></i><fmt:message key="article.supprimer" />
                                </button>
                            </form>
                        </div>
                    </c:if>
                </div>
            </article>
            
            <!-- Commentaires -->
            <div class="card shadow-sm">
                <div class="card-header">
                    <h5 class="mb-0">
                        <fmt:message key="article.commentaires" /> 
                        <span class="badge bg-primary">${fn:length(commentaires)}</span>
                    </h5>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty commentaires}">
                            <p class="text-muted"><fmt:message key="article.pasCommentaires" /></p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="commentaire" items="${commentaires}">
                                <div class="mb-3 pb-3 border-bottom">
                                    <div class="d-flex justify-content-between align-items-start">
                                        <div class="flex-grow-1">
                                            <strong>${commentaire.auteur.prenom} ${commentaire.auteur.nom}</strong>
                                            <small class="text-muted ms-2">
                                                <fmt:formatDate value="${commentaire.dateCreation}" pattern="dd/MM/yyyy HH:mm" />
                                            </small>
                                            <p class="mt-2 mb-0">${fn:escapeXml(commentaire.contenu)}</p>
                                        </div>
                                        <c:if test="${not empty sessionScope.utilisateur && 
                                                      (sessionScope.utilisateur.id == commentaire.auteurId || sessionScope.utilisateur.role == 'ADMIN')}">
                                            <c:set var="confirmDeleteComment"><fmt:message key="article.confirmerSuppressionCommentaire" /></c:set>
                                            <form method="post" action="${pageContext.request.contextPath}/commentaires" 
                                                  style="display: inline;"
                                                  onsubmit="return confirm('${confirmDeleteComment}');">
                                                <input type="hidden" name="_method" value="DELETE">
                                                <input type="hidden" name="id" value="${commentaire.id}">
                                                <input type="hidden" name="articleId" value="${article.id}">
                                                <button type="submit" class="btn btn-sm btn-link text-danger">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </form>
                                        </c:if>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    
                    <!-- Formulaire d'ajout de commentaire -->
                    <c:if test="${not empty sessionScope.utilisateur}">
                        <hr>
                        <h6><fmt:message key="commentaire.ajouter" /></h6>
                        <form method="post" action="${pageContext.request.contextPath}/commentaires">
                            <input type="hidden" name="articleId" value="${article.id}">
                            <div class="mb-3">
                                <textarea class="form-control" name="contenu" rows="3" 
                                          placeholder="<fmt:message key='commentaire.placeholder' />" required></textarea>
                            </div>
                            <button type="submit" class="btn btn-primary">
                                <fmt:message key="commentaire.publier" />
                            </button>
                        </form>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
