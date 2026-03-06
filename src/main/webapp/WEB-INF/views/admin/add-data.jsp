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

<c:set var="pageTitle" value="Ajouter des données" scope="request" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />
<jsp:include page="/WEB-INF/views/includes/navbar.jsp" />

<div class="container mt-4">
    <div class="row">
        <div class="col-12">
            <h2 class="mb-4">
                <i class="fas fa-database me-2"></i>Ajouter des données dans la base de données
            </h2>
            
            <!-- Message de succès/erreur -->
            <c:if test="${not empty message}">
                <div class="alert alert-${success ? 'success' : 'danger'} alert-dismissible fade show" role="alert">
                    <i class="fas fa-${success ? 'check-circle' : 'exclamation-circle'} me-2"></i>
                    ${message}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            
            <!-- Onglets -->
            <ul class="nav nav-tabs mb-4" id="dataTabs" role="tablist">
                <li class="nav-item" role="presentation">
                    <button class="nav-link active" id="user-tab" data-bs-toggle="tab" data-bs-target="#user-panel" type="button" role="tab">
                        <i class="fas fa-user me-2"></i>Ajouter un utilisateur
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="article-tab" data-bs-toggle="tab" data-bs-target="#article-panel" type="button" role="tab">
                        <i class="fas fa-file-alt me-2"></i>Ajouter un article
                    </button>
                </li>
            </ul>
            
            <!-- Contenu des onglets -->
            <div class="tab-content" id="dataTabsContent">
                <!-- Formulaire utilisateur -->
                <div class="tab-pane fade show active" id="user-panel" role="tabpanel">
                    <div class="card shadow">
                        <div class="card-header bg-primary text-white">
                            <h5 class="mb-0"><i class="fas fa-user-plus me-2"></i>Nouvel utilisateur</h5>
                        </div>
                        <div class="card-body">
                            <form method="post" action="${pageContext.request.contextPath}/admin/add-data">
                                <input type="hidden" name="action" value="add-user">
                                
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="nom" class="form-label">Nom *</label>
                                        <input type="text" class="form-control" id="nom" name="nom" required>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="prenom" class="form-label">Prénom *</label>
                                        <input type="text" class="form-control" id="prenom" name="prenom" required>
                                    </div>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="email" class="form-label">Email *</label>
                                    <input type="email" class="form-control" id="email" name="email" required>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="mot_de_passe" class="form-label">Mot de passe *</label>
                                    <input type="password" class="form-control" id="mot_de_passe" name="mot_de_passe" required>
                                    <small class="form-text text-muted">Le mot de passe sera automatiquement hashé avec BCrypt</small>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="role" class="form-label">Rôle</label>
                                    <select class="form-select" id="role" name="role">
                                        <option value="MEMBRE" selected>Membre</option>
                                        <option value="ADMIN">Administrateur</option>
                                    </select>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="bio" class="form-label">Biographie</label>
                                    <textarea class="form-control" id="bio" name="bio" rows="3"></textarea>
                                </div>
                                
                                <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fas fa-save me-2"></i>Créer l'utilisateur
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                
                <!-- Formulaire article -->
                <div class="tab-pane fade" id="article-panel" role="tabpanel">
                    <div class="card shadow">
                        <div class="card-header bg-success text-white">
                            <h5 class="mb-0"><i class="fas fa-file-alt me-2"></i>Nouvel article</h5>
                        </div>
                        <div class="card-body">
                            <form method="post" action="${pageContext.request.contextPath}/admin/add-data">
                                <input type="hidden" name="action" value="add-article">
                                
                                <div class="mb-3">
                                    <label for="titre" class="form-label">Titre *</label>
                                    <input type="text" class="form-control" id="titre" name="titre" required>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="resume" class="form-label">Résumé</label>
                                    <textarea class="form-control" id="resume" name="resume" rows="2"></textarea>
                                    <small class="form-text text-muted">Résumé court de l'article (max 500 caractères)</small>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="contenu" class="form-label">Contenu *</label>
                                    <textarea class="form-control" id="contenu" name="contenu" rows="10" required></textarea>
                                </div>
                                
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="auteur_id" class="form-label">Auteur *</label>
                                        <select class="form-select" id="auteur_id" name="auteur_id" required>
                                            <option value="">-- Sélectionner un auteur --</option>
                                            <c:forEach var="user" items="${utilisateurs}">
                                                <option value="${user.id}">${user.prenom} ${user.nom} (${user.email})</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="statut" class="form-label">Statut</label>
                                        <select class="form-select" id="statut" name="statut">
                                            <option value="PUBLIE" selected>Publié</option>
                                            <option value="BROUILLON">Brouillon</option>
                                            <option value="ARCHIVE">Archivé</option>
                                        </select>
                                    </div>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="image_url" class="form-label">URL de l'image</label>
                                    <input type="url" class="form-control" id="image_url" name="image_url" 
                                           placeholder="https://example.com/image.jpg">
                                </div>
                                
                                <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                    <button type="submit" class="btn btn-success">
                                        <i class="fas fa-save me-2"></i>Créer l'article
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Informations utiles -->
            <div class="card mt-4 shadow-sm">
                <div class="card-header bg-info text-white">
                    <h5 class="mb-0"><i class="fas fa-info-circle me-2"></i>Informations</h5>
                </div>
                <div class="card-body">
                    <p><strong>Note importante :</strong></p>
                    <ul>
                        <li>Les utilisateurs créés ici seront automatiquement activés (pas besoin de vérification email)</li>
                        <li>Les mots de passe sont automatiquement hashés avec BCrypt</li>
                        <li>Vous pouvez également ajouter des données directement via SQL en utilisant le fichier <code>insert_data.sql</code></li>
                        <li>Pour accéder à la base de données sur Railway, utilisez l'onglet "Query" dans l'interface MySQL de Railway</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
