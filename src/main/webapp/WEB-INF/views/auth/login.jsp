<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.langue}" />
<fmt:setBundle basename="messages" />

<c:set var="pageTitle" value="Connexion" scope="request" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />
<jsp:include page="/WEB-INF/views/includes/navbar.jsp" />

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5">
            <div class="card shadow">
                <div class="card-body p-5">
                    <h2 class="card-title text-center mb-4">
                        <fmt:message key="login.titre" />
                    </h2>
                    
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
                    
                    <form method="post" action="${pageContext.request.contextPath}/login">
                        <div class="mb-3">
                            <label for="email" class="form-label">
                                <fmt:message key="login.email" />
                            </label>
                            <input type="email" class="form-control" id="email" name="email" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="motDePasse" class="form-label">
                                <fmt:message key="login.motdepasse" />
                            </label>
                            <input type="password" class="form-control" id="motDePasse" name="motDePasse" required>
                        </div>
                        
                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary">
                                <fmt:message key="login.bouton" />
                            </button>
                        </div>
                    </form>
                    
                    <hr class="my-4">
                    
                    <p class="text-center text-muted mb-0">
                        <fmt:message key="login.pasCompte" />
                        <a href="${pageContext.request.contextPath}/inscription">
                            <fmt:message key="login.inscrire" />
                        </a>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
