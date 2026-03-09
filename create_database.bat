@echo off
echo ========================================
echo Creation de la base de donnees MySQL blog_jee
echo ========================================
echo.
echo Ce script utilise MySQL
echo.

REM Vérifier si mysql est disponible dans le PATH
where mysql >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    set MYSQL_CMD=mysql
    goto :found
)

REM Essayer les chemins courants de XAMPP
if exist "C:\xampp\mysql\bin\mysql.exe" (
    set MYSQL_CMD=C:\xampp\mysql\bin\mysql.exe
    goto :found
)
if exist "C:\xampp2\mysql\bin\mysql.exe" (
    set MYSQL_CMD=C:\xampp2\mysql\bin\mysql.exe
    goto :found
)

REM Essayer les chemins courants de MySQL
if exist "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" (
    set "MYSQL_CMD=C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
    goto :found
)
if exist "C:\Program Files\MySQL\MySQL Server 8.2\bin\mysql.exe" (
    set "MYSQL_CMD=C:\Program Files\MySQL\MySQL Server 8.2\bin\mysql.exe"
    goto :found
)

echo ERREUR: mysql n'est pas trouve dans le PATH
echo.
echo Pour utiliser ce script, assurez-vous que MySQL est installe et accessible.
echo Chemins verifies:
echo   - PATH systeme
echo   - C:\xampp\mysql\bin\
echo   - C:\xampp2\mysql\bin\
echo   - C:\Program Files\MySQL\MySQL Server 8.0\bin\
echo   - C:\Program Files\MySQL\MySQL Server 8.2\bin\
echo.
echo Alternative: La base de donnees sera creee automatiquement
echo lors du premier lancement de l'application.
echo.
pause
exit /b 1

:found
echo MySQL trouve: %MYSQL_CMD%
echo.
echo Execution du script de creation de la base de donnees...
echo Entrez le mot de passe root MySQL (appuyez sur Entree si vide):
echo.

"%MYSQL_CMD%" -u root -p < schema.sql

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Base de donnees creee avec succes!
    echo ========================================
    echo.
    echo Base de donnees: blog_jee
    echo.
    echo Vous pouvez maintenant utiliser:
    echo - Email: admin@blog.com
    echo - Mot de passe: password123
    echo.
) else (
    echo.
    echo ERREUR lors de la creation de la base
    echo.
    echo Verifiez que:
    echo 1. MySQL est demarre
    echo 2. Le mot de passe root est correct
    echo 3. Vous avez les droits d'administration
    echo.
    echo Note: La base de donnees sera creee automatiquement
    echo lors du premier lancement de l'application si elle n'existe pas.
    echo.
)

pause
