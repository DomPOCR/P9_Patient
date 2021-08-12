FROM openjdk:8-jdk-alpine
EXPOSE 8080
VOLUME /var/lib/mysql/data
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} patient.jar
ENTRYPOINT ["java","-jar","/patient.jar"]