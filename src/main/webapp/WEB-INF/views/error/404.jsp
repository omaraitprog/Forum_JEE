<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.langue}" />
<fmt:setBundle basename="messages" />

<c:set var="pageTitle" value="Page non trouvée" scope="request" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />
<jsp:include page="/WEB-INF/views/includes/navbar.jsp" />

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6 text-center">
            <h1 class="display-1">404</h1>
            <h2>Page non trouvée</h2>
            <p class="text-muted">La page que vous recherchez n'existe pas ou a été déplacée.</p>
            <a href="${pageContext.request.contextPath}/articles" class="btn btn-primary">
                <i class="fas fa-home me-2"></i>Retour à l'accueil
            </a>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
