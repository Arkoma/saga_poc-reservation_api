FROM eclipse-temurin:11-alpine
RUN mkdir /opt/app
COPY build/libs/reservation-service-0.0.1-SNAPSHOT.jar /opt/app
CMD ["java", "-jar", "/opt/app/reservation-service-0.0.1-SNAPSHOT.jar"]