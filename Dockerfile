FROM eclipse-temurin:17-jdk-alpine

COPY target/desafio-picpay-0.0.1-SNAPSHOT.jar /app/app.jar

CMD ["java", "-jar", "app/app.jar"]