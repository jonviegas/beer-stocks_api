FROM maven:3.8.2-jdk-11
COPY target/beerstocks-0.0.1-SNAPSHOT.jar /app/
WORKDIR /app