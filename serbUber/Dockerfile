FROM openjdk:17-oracle
ENV MONGO_URL mongodb://localhost/serbUberMongodb
EXPOSE 8080
RUN mkdir -p /app/
ADD build/libs/springboot-mongo-docker-0.0.1-SNAPSHOT.jar /app/springboot-mongo-docker-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/app/springboot-mongo-docker-0.0.1-SNAPSHOT.jar"]
