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

# Limiter le heap Java à 256 Mo pour éviter les problèmes de limite mémoire
ENV JAVA_OPTS="-Xmx256m -Xms256m"
ENV CATALINA_OPTS="-Xmx256m -Xms256m"

# Définir le répertoire de travail
WORKDIR /usr/local/tomcat

# Copier le fichier WAR depuis le stage de build
COPY --from=build /app/target/Forum_Project-1.0-SNAPSHOT.war webapps/ROOT.war

# Exposer le port 8080 (port par défaut de Tomcat)
# Railway mappe automatiquement le port via la variable d'environnement PORT
EXPOSE 8080

# Créer un script pour configurer le port depuis la variable d'environnement PORT si nécessaire
RUN echo '#!/bin/bash\n\
# Trouver et définir JAVA_HOME automatiquement depuis le chemin Java\n\
if [ -z "$JAVA_HOME" ] || [ ! -d "$JAVA_HOME" ] || [ ! -f "$JAVA_HOME/bin/java" ]; then\n\
  # Méthode 1: Utiliser java -XshowSettings:properties pour obtenir JAVA_HOME\n\
  if command -v java >/dev/null 2>&1; then\n\
    DETECTED_HOME=$(java -XshowSettings:properties -version 2>&1 | grep "java.home" | awk '\''{print $3}'\'')\n\
    if [ -n "$DETECTED_HOME" ] && [ -d "$DETECTED_HOME" ]; then\n\
      export JAVA_HOME="$DETECTED_HOME"\n\
    fi\n\
  fi\n\
  # Méthode 2: Trouver depuis le chemin de l'\''exécutable java\n\
  if [ -z "$JAVA_HOME" ] || [ ! -d "$JAVA_HOME" ]; then\n\
    if command -v java >/dev/null 2>&1; then\n\
      JAVA_PATH=$(which java)\n\
      # Résoudre les liens symboliques\n\
      while [ -L "$JAVA_PATH" ]; do\n\
        JAVA_PATH=$(readlink "$JAVA_PATH")\n\
        if [ "${JAVA_PATH#/}" = "$JAVA_PATH" ]; then\n\
          JAVA_PATH=$(dirname $(which java))/$JAVA_PATH\n\
        fi\n\
      done\n\
      # Extraire JAVA_HOME (remonter de bin/java)\n\
      JAVA_HOME=$(cd "$(dirname "$JAVA_PATH")/.." && pwd)\n\
      export JAVA_HOME\n\
    fi\n\
  fi\n\
  # Méthode 3: Essayer les chemins standards\n\
  if [ -z "$JAVA_HOME" ] || [ ! -d "$JAVA_HOME" ]; then\n\
    for POSSIBLE_HOME in /usr/local/openjdk-21 /usr/lib/jvm/java-21-openjdk-amd64 /opt/java/openjdk /usr/lib/jvm/java-21; do\n\
      if [ -d "$POSSIBLE_HOME" ] && [ -f "$POSSIBLE_HOME/bin/java" ]; then\n\
        export JAVA_HOME="$POSSIBLE_HOME"\n\
        break\n\
      fi\n\
    done\n\
  fi\n\
fi\n\
# Vérifier que JAVA_HOME est valide\n\
if [ -z "$JAVA_HOME" ] || [ ! -d "$JAVA_HOME" ] || [ ! -f "$JAVA_HOME/bin/java" ]; then\n\
  echo "Erreur: JAVA_HOME n'\''est pas défini correctement. JAVA_HOME=$JAVA_HOME"\n\
  echo "Java trouvé à: $(which java 2>/dev/null || echo '\''non trouvé'\'')"\n\
  exit 1\n\
fi\n\
PORT=${PORT:-8080}\n\
if [ "$PORT" != "8080" ]; then\n\
  sed -i "s/port=\"8080\"/port=\"$PORT\"/" /usr/local/tomcat/conf/server.xml\n\
fi\n\
# S'\''assurer que les options Java sont appliquées\n\
export JAVA_OPTS="${JAVA_OPTS:--Xmx256m -Xms256m}"\n\
export CATALINA_OPTS="${CATALINA_OPTS:--Xmx256m -Xms256m}"\n\
exec catalina.sh run' > /usr/local/tomcat/start.sh && \
    chmod +x /usr/local/tomcat/start.sh

# Tomcat démarre automatiquement avec le port configuré
CMD ["/usr/local/tomcat/start.sh"]
