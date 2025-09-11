# Dockerfile para StockService - Optimizado para Render
FROM openjdk:17-jdk-slim

# Instalar curl para health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven primero (para cache de dependencias)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Hacer ejecutable el wrapper de Maven
RUN chmod +x ./mvnw

# Descargar dependencias (se cachea si pom.xml no cambia)
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Construir la aplicación
RUN ./mvnw clean package -DskipTests

# Exponer puerto (Render usa PORT como variable de entorno)
EXPOSE 8081

# Variables de entorno para Render
ENV SPRING_PROFILES_ACTIVE=production
ENV SERVER_ADDRESS=0.0.0.0
ENV SERVER_PORT=${PORT:-8081}

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:${PORT:-8081}/actuator/health || exit 1

# Ejecutar la aplicación
CMD ["sh", "-c", "java -Dserver.port=${PORT:-8081} -jar target/StockService-0.0.1-SNAPSHOT.jar"]
