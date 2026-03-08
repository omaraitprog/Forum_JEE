@echo off
echo ========================================
echo Creation de la base de donnees SQLite blog_jee
echo ========================================
echo.

REM Vérifier si sqlite3 est disponible
where sqlite3 >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: sqlite3 n'est pas trouve dans le PATH
    echo.
    echo Pour installer SQLite:
    echo 1. Telechargez SQLite depuis https://www.sqlite.org/download.html
    echo 2. Ajoutez sqlite3.exe au PATH ou placez-le dans ce dossier
    echo.
    echo Alternative: La base de donnees sera creee automatiquement
    echo lors du premier lancement de l'application.
    echo.
    pause
    exit /b 1
)

REM Créer la base de données SQLite
echo Creation de la base de donnees blog_jee.db...
sqlite3 blog_jee.db < schema.sql

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Base de donnees creee avec succes!
    echo ========================================
    echo.
    echo Fichier cree: blog_jee.db
    echo.
    echo Vous pouvez maintenant utiliser:
    echo - Email: admin@blog.com
    echo - Mot de passe: password123
    echo.
) else (
    echo.
    echo ========================================
    echo ERREUR lors de la creation de la base
    echo ========================================
    echo.
    echo Verifiez que:
    echo 1. sqlite3.exe est accessible
    echo 2. Le fichier schema.sql existe
    echo 3. Vous avez les droits d'ecriture dans ce dossier
    echo.
    echo Note: La base de donnees sera creee automatiquement
    echo lors du premier lancement de l'application si elle n'existe pas.
    echo.
)

pause
