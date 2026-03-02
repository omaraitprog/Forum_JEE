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

<c:set var="pageTitle" value="<fmt:message key='profil.titre' />" scope="request" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />
<jsp:include page="/WEB-INF/views/includes/navbar.jsp" />

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <h2 class="mb-4"><fmt:message key="profil.titre" /></h2>
            
            <c:if test="${not empty erreur}">
                <div class="alert alert-danger" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>${erreur}
                </div>
            </c:if>
            
            <c:if test="${not empty succes}">
                <div class="alert alert-success" role="alert">
                    <i class="fas fa-check-circle me-2"></i>${succes}
                </div>
            </c:if>
            
            <!-- Informations du profil -->
            <div class="card shadow mb-4">
                <div class="card-header">
                    <h5 class="mb-0"><fmt:message key="profil.modifier" /></h5>
                </div>
                <div class="card-body">
                    <form method="post" action="${pageContext.request.contextPath}/profil">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="prenom" class="form-label">
                                    <fmt:message key="register.prenom" /> *
                                </label>
                                <input type="text" class="form-control" id="prenom" name="prenom" 
                                       value="<c:out value="${utilisateur.prenom}" />" required>
                            </div>
                            
                            <div class="col-md-6 mb-3">
                                <label for="nom" class="form-label">
                                    <fmt:message key="register.nom" /> *
                                </label>
                                <input type="text" class="form-control" id="nom" name="nom" 
                                       value="<c:out value="${utilisateur.nom}" />" required>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="email" class="form-label">
                                <fmt:message key="register.email" /> *
                            </label>
                            <input type="email" class="form-control" id="email" name="email" 
                                   value="<c:out value="${utilisateur.email}" />" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="photoProfil" class="form-label">
                                <fmt:message key="profil.photo" />
                            </label>
                            <input type="url" class="form-control" id="photoProfil" name="photoProfil" 
                                   value="<c:out value="${utilisateur.photoProfil}" />"
                                   placeholder="https://example.com/photo.jpg">
                        </div>
                        
                        <div class="mb-3">
                            <label for="bio" class="form-label">
                                <fmt:message key="profil.bio" />
                            </label>
                            <textarea class="form-control" id="bio" name="bio" rows="4"><c:out value="${utilisateur.bio}" /></textarea>
                        </div>
                        
                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary">
                                <fmt:message key="profil.enregistrer" />
                            </button>
                        </div>
                    </form>
                </div>
            </div>
            
            <!-- Changement de mot de passe -->
            <div class="card shadow">
                <div class="card-header">
                    <h5 class="mb-0"><fmt:message key="profil.changerMotDePasseTitre" /></h5>
                </div>
                <div class="card-body">
                    <form method="post" action="${pageContext.request.contextPath}/profil">
                        <input type="hidden" name="action" value="change-password">
                        
                        <div class="mb-3">
                            <label for="ancienMotDePasse" class="form-label">
                                <fmt:message key="profil.ancienMotDePasse" /> *
                            </label>
                            <input type="password" class="form-control" id="ancienMotDePasse" name="ancienMotDePasse" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="nouveauMotDePasse" class="form-label">
                                <fmt:message key="profil.nouveauMotDePasse" /> *
                            </label>
                            <input type="password" class="form-control" id="nouveauMotDePasse" name="nouveauMotDePasse" 
                                   required minlength="6">
                            <small class="form-text text-muted"><fmt:message key="article.minCaracteres" /></small>
                        </div>
                        
                        <div class="mb-3">
                            <label for="confirmerMotDePasse" class="form-label">
                                <fmt:message key="profil.confirmerMotDePasse" /> *
                            </label>
                            <input type="password" class="form-control" id="confirmerMotDePasse" name="confirmation" 
                                   required minlength="6">
                        </div>
                        
                        <div class="d-grid">
                            <button type="submit" class="btn btn-warning">
                                <fmt:message key="profil.changerMotDePasse" />
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<c:set var="errorPasswordMismatch"><fmt:message key="erreur.motDePasseNonCorrespond" /></c:set>
<script>
// Validation côté client pour vérifier que les mots de passe correspondent
document.getElementById('confirmerMotDePasse').addEventListener('input', function() {
    const nouveauMotDePasse = document.getElementById('nouveauMotDePasse').value;
    const confirmation = this.value;
    
    if (nouveauMotDePasse !== confirmation) {
        this.setCustomValidity('${errorPasswordMismatch}');
    } else {
        this.setCustomValidity('');
    }
});
</script>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
