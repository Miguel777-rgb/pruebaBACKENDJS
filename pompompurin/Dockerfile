# ---------- Etapa 1: compilar el .jar (imagen oficial con Gradle 9.8.2 y JDK 25 ya instalados) ----------
FROM gradle:9.8.2-jdk25 AS build
WORKDIR /app
COPY . .
RUN gradle bootJar --no-daemon

# ---------- Etapa 2: imagen liviana que solo ejecuta el .jar (JRE 25) ----------
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
