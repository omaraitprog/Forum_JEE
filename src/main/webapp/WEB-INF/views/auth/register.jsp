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

<c:set var="pageTitle" value="<fmt:message key='nav.inscription' />" scope="request" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />
<jsp:include page="/WEB-INF/views/includes/navbar.jsp" />

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="card shadow">
                <div class="card-body p-5">
                    <h2 class="card-title text-center mb-4">
                        <fmt:message key="register.titre" />
                    </h2>
                    
                    <c:if test="${not empty erreur}">
                        <div class="alert alert-danger" role="alert">
                            <i class="fas fa-exclamation-circle me-2"></i>${erreur}
                        </div>
                    </c:if>
                    
                    <c:if test="${not empty succes}">
                        <div class="alert alert-success" role="alert">
                            <i class="fas fa-check-circle me-2"></i>
                            <c:if test="${not empty verificationLink}">
                                <p>Inscription réussie !</p>
                                <p>Cliquez sur ce lien pour activer votre compte :</p>
                                <p><a href="${verificationLink}" class="btn btn-sm btn-primary">Activer mon compte</a></p>
                                <p><small>Ou copiez ce lien : <code>${verificationLink}</code></small></p>
                                <p><small class="text-muted">Note: Si l'email n'a pas été envoyé, utilisez le lien ci-dessus.</small></p>
                            </c:if>
                            <c:if test="${empty verificationLink}">
                                ${succes}
                            </c:if>
                        </div>
                    </c:if>
                    
                    <div class="alert alert-info" role="alert">
                        <i class="fas fa-info-circle me-2"></i><fmt:message key="register.infoEmail" />
                    </div>
                    
                    <form method="post" action="${pageContext.request.contextPath}/inscription">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="prenom" class="form-label">
                                    <fmt:message key="register.prenom" /> *
                                </label>
                                <input type="text" class="form-control" id="prenom" name="prenom" required>
                            </div>
                            
                            <div class="col-md-6 mb-3">
                                <label for="nom" class="form-label">
                                    <fmt:message key="register.nom" /> *
                                </label>
                                <input type="text" class="form-control" id="nom" name="nom" required>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="email" class="form-label">
                                <fmt:message key="register.email" /> *
                            </label>
                            <input type="email" class="form-control" id="email" name="email" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="motDePasse" class="form-label">
                                <fmt:message key="register.motdepasse" /> *
                            </label>
                            <input type="password" class="form-control" id="motDePasse" name="motDePasse" required minlength="6">
                            <small class="form-text text-muted"><fmt:message key="article.minCaracteres" /></small>
                        </div>
                        
                        <div class="mb-3">
                            <label for="confirmation" class="form-label">
                                <fmt:message key="register.confirmer" /> *
                            </label>
                            <input type="password" class="form-control" id="confirmation" name="confirmation" required minlength="6">
                        </div>
                        
                        <div class="mb-3">
                            <label for="bio" class="form-label">
                                <fmt:message key="profil.bio" />
                            </label>
                            <textarea class="form-control" id="bio" name="bio" rows="3"></textarea>
                        </div>
                        
                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary">
                                <fmt:message key="register.bouton" />
                            </button>
                        </div>
                    </form>
                    
                    <hr class="my-4">
                    
                    <p class="text-center text-muted mb-0">
                        <fmt:message key="register.dejaCompte" />
                        <a href="${pageContext.request.contextPath}/login">
                            <fmt:message key="register.connecter" />
                        </a>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

<c:set var="errorPasswordMismatch"><fmt:message key="erreur.motDePasseNonCorrespond" /></c:set>
<script>
// Validation côté client pour vérifier que les mots de passe correspondent
document.getElementById('confirmation').addEventListener('input', function() {
    const motDePasse = document.getElementById('motDePasse').value;
    const confirmation = this.value;
    
    if (motDePasse !== confirmation) {
        this.setCustomValidity('${errorPasswordMismatch}');
    } else {
        this.setCustomValidity('');
    }
});
</script>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
