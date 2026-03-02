@echo off
echo Creation de la base de donnees blog_jee...
echo.
echo Veuillez entrer votre mot de passe MySQL root:
mysql -u root -p < schema.sql
echo.
echo Base de donnees creee avec succes!
pause
