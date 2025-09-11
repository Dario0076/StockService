# Dockerfile para microservicio Spring Boot
FROM eclipse-temurin:17-jdk

# Instalar Maven
RUN apt-get update && apt-get install -y maven

WORKDIR /app
COPY . .

# Usar Maven instalado en lugar del wrapper
RUN mvn clean package -DskipTests

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "target/StockService-0.0.1-SNAPSHOT.jar"]
