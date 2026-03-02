// Script JavaScript principal pour le Blog JEE

// Fonction pour préserver les paramètres de l'URL lors du changement de langue
function changeLanguage(lang) {
    const url = new URL(window.location.href);
    url.searchParams.set('lang', lang);
    window.location.href = url.toString();
}

// Validation des formulaires
document.addEventListener('DOMContentLoaded', function() {
    // Validation des mots de passe
    const passwordInputs = document.querySelectorAll('input[type="password"]');
    passwordInputs.forEach(input => {
        if (input.name === 'confirmation' || input.name === 'confirmerMotDePasse') {
            input.addEventListener('input', function() {
                const passwordField = document.querySelector('input[name="motDePasse"], input[name="nouveauMotDePasse"]');
                if (passwordField && this.value !== passwordField.value) {
                    this.setCustomValidity('Les mots de passe ne correspondent pas');
                } else {
                    this.setCustomValidity('');
                }
            });
        }
    });
    
    // Auto-dismiss des alertes après 5 secondes
    const alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.5s';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        }, 5000);
    });
    
    // Confirmation avant suppression
    const deleteForms = document.querySelectorAll('form[onsubmit*="confirm"]');
    deleteForms.forEach(form => {
        form.addEventListener('submit', function(e) {
            if (!confirm('Êtes-vous sûr de vouloir effectuer cette action ?')) {
                e.preventDefault();
            }
        });
    });
    
    // Animation de fade-in pour les cartes
    const cards = document.querySelectorAll('.card');
    cards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        setTimeout(() => {
            card.style.transition = 'opacity 0.5s, transform 0.5s';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, index * 100);
    });
});

// Fonction pour formater les dates
function formatDate(dateString) {
    const date = new Date(dateString);
    const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' };
    return date.toLocaleDateString('fr-FR', options);
}

// Fonction pour limiter le nombre de caractères dans un textarea
function limitCharacters(textarea, maxLength) {
    if (textarea.value.length > maxLength) {
        textarea.value = textarea.value.substring(0, maxLength);
    }
    const remaining = maxLength - textarea.value.length;
    const counter = textarea.parentElement.querySelector('.char-counter');
    if (counter) {
        counter.textContent = remaining + ' caractères restants';
    }
}

// Gestion des erreurs AJAX (si nécessaire plus tard)
function handleAjaxError(xhr, status, error) {
    console.error('Erreur AJAX:', error);
    alert('Une erreur est survenue. Veuillez réessayer.');
}
