FROM openjdk:8-jdk-alpine
EXPOSE 8080
VOLUME /var/lib/mysql/data
ARG JAR_FILE=target/P9_Patient-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} p9_patient.jar
ENTRYPOINT ["java","-jar","/p9_patient.jar"]