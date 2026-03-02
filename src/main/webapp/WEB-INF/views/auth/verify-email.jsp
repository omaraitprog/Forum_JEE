<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Vérification Email" scope="request" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />
<jsp:include page="/WEB-INF/views/includes/navbar.jsp" />

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5">
            <div class="card shadow">
                <div class="card-body p-5 text-center">
                    <c:if test="${not empty succes}">
                        <div class="alert alert-success" role="alert">
                            <i class="fas fa-check-circle fa-3x mb-3 text-success"></i>
                            <h4 class="alert-heading">${succes}</h4>
                            <hr>
                            <p class="mb-0">
                                <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">
                                    Connexion
                                </a>
                            </p>
                        </div>
                    </c:if>
                    
                    <c:if test="${not empty erreur}">
                        <div class="alert alert-danger" role="alert">
                            <i class="fas fa-exclamation-triangle fa-3x mb-3 text-danger"></i>
                            <h4 class="alert-heading">${erreur}</h4>
                            <hr>
                            <p class="mb-0">
                                <a href="${pageContext.request.contextPath}/inscription" class="btn btn-primary">
                                    Inscription
                                </a>
                            </p>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
