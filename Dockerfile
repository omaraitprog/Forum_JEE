# Stage 1: Build avec Maven
FROM maven:3.9-eclipse-temurin-21 AS build

# Vérifier que Maven et Java sont disponibles
RUN mvn -version && java -version

WORKDIR /app

# Copier le fichier pom.xml d'abord pour optimiser le cache Docker
COPY pom.xml .

# Télécharger les dépendances (cache layer) - ignore les erreurs si certaines dépendances ne sont pas disponibles
RUN mvn dependency:go-offline -B || true

# Copier le code source
COPY src ./src

# Construire le projet avec Maven (pas mvnw)
# Utiliser mvn directement depuis l'image Maven
RUN mvn clean package -DskipTests -B

# Stage 2: Déploiement avec Tomcat
FROM tomcat:10.1-jdk21-temurin

# Définir JAVA_HOME pour Tomcat
ENV JAVA_HOME=/usr/local/openjdk-21
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Définir le répertoire de travail
WORKDIR /usr/local/tomcat

# Copier le fichier WAR depuis le stage de build
COPY --from=build /app/target/Forum_Project-1.0-SNAPSHOT.war webapps/ROOT.war

# Exposer le port 8080 (port par défaut de Tomcat)
# Railway utilisera automatiquement la variable d'environnement PORT
EXPOSE 8080

# Tomcat démarre automatiquement
CMD ["catalina.sh", "run"]
