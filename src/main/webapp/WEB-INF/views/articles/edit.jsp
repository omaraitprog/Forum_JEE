<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.langue}" />
<fmt:setBundle basename="messages" />

<c:set var="pageTitle" value="Modifier Article" scope="request" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />
<jsp:include page="/WEB-INF/views/includes/navbar.jsp" />

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow">
                <div class="card-header">
                    <h4 class="mb-0"><fmt:message key="article.modifier" /></h4>
                </div>
                <div class="card-body">
                    <c:if test="${not empty erreur}">
                        <div class="alert alert-danger" role="alert">
                            <i class="fas fa-exclamation-circle me-2"></i>${erreur}
                        </div>
                    </c:if>
                    
                    <form method="post" action="${pageContext.request.contextPath}/articles">
                        <input type="hidden" name="_method" value="PUT">
                        <input type="hidden" name="id" value="${article.id}">
                        
                        <div class="mb-3">
                            <label for="titre" class="form-label">
                                <fmt:message key="article.titre" /> *
                            </label>
                            <input type="text" class="form-control" id="titre" name="titre" 
                                   value="<c:out value="${article.titre}" />" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="resume" class="form-label">
                                <fmt:message key="article.resume" />
                            </label>
                            <textarea class="form-control" id="resume" name="resume" rows="2"><c:out value="${article.resume}" /></textarea>
                            <small class="form-text text-muted">Maximum 500 caractères</small>
                        </div>
                        
                        <div class="mb-3">
                            <label for="contenu" class="form-label">
                                <fmt:message key="article.contenu" /> *
                            </label>
                            <textarea class="form-control" id="contenu" name="contenu" rows="10" required><c:out value="${article.contenu}" /></textarea>
                        </div>
                        
                        <div class="mb-3">
                            <label for="imageUrl" class="form-label">
                                URL de l'image
                            </label>
                            <input type="url" class="form-control" id="imageUrl" name="imageUrl" 
                                   value="<c:out value="${article.imageUrl}" />"
                                   placeholder="https://example.com/image.jpg">
                        </div>
                        
                        <div class="mb-3">
                            <label for="statut" class="form-label">Statut</label>
                            <select class="form-select" id="statut" name="statut">
                                <option value="PUBLIE" ${article.statut == 'PUBLIE' ? 'selected' : ''}>Publié</option>
                                <option value="BROUILLON" ${article.statut == 'BROUILLON' ? 'selected' : ''}>Brouillon</option>
                                <option value="ARCHIVE" ${article.statut == 'ARCHIVE' ? 'selected' : ''}>Archivé</option>
                            </select>
                        </div>
                        
                        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                            <a href="${pageContext.request.contextPath}/articles?id=${article.id}" class="btn btn-secondary">
                                Annuler
                            </a>
                            <button type="submit" class="btn btn-primary">
                                <fmt:message key="profil.enregistrer" />
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
