# --- Estágio de Build ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Copia só o pom.xml primeiro para cachear dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# 2. Copia o código e builda
COPY src ./src
RUN mvn clean package -DskipTests

# --- Estágio de Runtime ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 4. Copia especificamente o jar gerado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
