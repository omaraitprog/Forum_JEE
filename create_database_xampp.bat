@echo off
echo ========================================
echo Creation de la base de donnees blog_jee
echo (via XAMPP MySQL)
echo ========================================
echo.

REM Chemin vers MySQL dans XAMPP
set MYSQL_PATH=C:\xampp\mysql\bin\mysql.exe

REM Vérifier si MySQL existe dans XAMPP
if not exist "%MYSQL_PATH%" (
    set MYSQL_PATH=C:\xampp2\mysql\bin\mysql.exe
)
if not exist "%MYSQL_PATH%" (
    echo ERREUR: MySQL n'est pas trouve dans XAMPP
    echo Chemins verifies:
    echo   - C:\xampp\mysql\bin\mysql.exe
    echo   - C:\xampp2\mysql\bin\mysql.exe
    echo.
    echo Veuillez verifier le chemin d'installation de XAMPP.
    pause
    exit /b 1
)

echo MySQL trouve: %MYSQL_PATH%
echo.
echo Veuillez entrer votre mot de passe MySQL root:
echo (Appuyez sur Entree si le mot de passe est vide)
echo.

REM Exécuter le script SQL
"%MYSQL_PATH%" -u root -p < schema.sql

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Base de donnees creee avec succes!
    echo ========================================
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
    echo 1. MySQL est demarre dans XAMPP
    echo 2. Le mot de passe est correct
    echo 3. Vous avez les droits d'administration
    echo.
)

pause
