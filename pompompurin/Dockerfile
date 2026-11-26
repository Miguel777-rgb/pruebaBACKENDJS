# ---------- Etapa 1: compilar el .jar con Gradle (JDK 25) ----------
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew && ./gradlew bootJar --no-daemon

# ---------- Etapa 2: imagen liviana que solo ejecuta el .jar (JRE 25) ----------
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
