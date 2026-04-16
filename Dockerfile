
# =============================================================
# Stage 1 — Build
# =============================================================
FROM maven:3.9-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copia o pom primeiro para cachear as dependências separadamente
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copia o código e compila
COPY src src
RUN mvn clean package -DskipTests -q

# =============================================================
# Stage 2 — Runtime
# =============================================================
FROM eclipse-temurin:17-jre-alpine AS runtime

# Usuário não-root para segurança
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copia apenas o jar gerado
COPY --from=build /app/target/*.jar app.jar

# Diretório para as chaves RSA (montado como secret/volume em produção)
RUN mkdir -p /run/secrets && chown -R appuser:appgroup /run/secrets

USER appuser

EXPOSE 8080

# Flags de JVM recomendadas para containers
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
