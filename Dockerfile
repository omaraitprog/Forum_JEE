# Stage 1: Build avec Maven
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copier les fichiers de configuration Maven
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Télécharger les dépendances (cache layer)
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src ./src

# Construire le projet
RUN mvn clean package -DskipTests

# Stage 2: Déploiement avec Tomcat
FROM tomcat:10.1-jdk21-temurin

# Définir le répertoire de travail
WORKDIR /usr/local/tomcat

# Copier le fichier WAR depuis le stage de build
COPY --from=build /app/target/Forum_Project-1.0-SNAPSHOT.war webapps/ROOT.war

# Exposer le port 8080 (port par défaut de Tomcat)
# Railway utilisera automatiquement la variable d'environnement PORT
EXPOSE 8080

# Tomcat démarre automatiquement
CMD ["catalina.sh", "run"]
