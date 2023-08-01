# Usa la imagen base de Adoptium Temurin con Java 17
FROM eclipse-temurin:17.0.8_7-jdk

# Define el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos necesarios para construir el proyecto
# (en este ejemplo, copiamos solo el archivo build.gradle y settings.gradle)
COPY build.gradle settings.gradle /app/

# Copia el directorio src que contiene el código fuente de tu aplicación
COPY src /app/src

# Ejecuta el comando 'gradle build' para construir la aplicación
RUN ./gradlew build

# Expone el puerto en el que escucha la aplicación Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación cuando el contenedor se inicie
CMD ["java", "-jar", "build/libs/administration-0.0.1-SNAPSHOT.jar"]
