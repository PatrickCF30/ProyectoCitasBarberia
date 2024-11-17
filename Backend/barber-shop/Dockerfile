# Etapa de construcción

FROM maven:3.8.5-openjdk-11-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa de ejecución
# Iimagen base con JDK 11
FROM openjdk:11-jdk-slim

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo JAR generado desde la etapa de construcción
COPY target/barber-shop-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto de la aplicación
EXPOSE 8190

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]