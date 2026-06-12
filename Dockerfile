# Fase 1: Construcción de la aplicación (Maven)
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Fase 2: Ejecución del servidor con el ejecutable generado
FROM openjdk:17-jdk-slim
COPY --from=build /target/*.jar app.jar
EXPOSE 8108
ENTRYPOINT ["java", "-jar", "app.jar"]
