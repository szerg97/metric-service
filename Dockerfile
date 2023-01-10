FROM amazoncorretto:17

WORKDIR /app
COPY target/metric-service-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "metric-service-0.0.1-SNAPSHOT.jar"]