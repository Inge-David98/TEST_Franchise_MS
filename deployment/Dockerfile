# Usa una imagen base de OpenJDK 17
FROM eclipse-temurin:17-jdk-alpine

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR de la aplicación al contenedor
COPY applications/app-service/build/libs/Franchise.jar /app/Franchise.jar

# Expone el puerto en el que corre la aplicación
EXPOSE 8080

# Define el comando por defecto para correr la aplicación
CMD ["java", "-jar", "/app/Franchise.jar"]
