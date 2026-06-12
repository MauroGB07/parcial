# Fase 1: Construcción de la aplicación (Maven)
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Fase 2: Ejecución del servidor con el ejecutable generado
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8108
ENTRYPOINT ["java", "-jar", "app.jar"]
