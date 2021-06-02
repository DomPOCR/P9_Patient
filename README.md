## MEDISCREEN APP : P9-Patient

Micro-service Patient manage patients informations on MEDISCREEN Application.

# Getting Started

- Endpoint : http://localhost:8080/

# Prerequis 

- Java 1.8 JDK
- Gradle 5.1
- Spring Boot 2.2.6
- Docker 20.10.5 

# Installation

## Modifie host file :

- 127.0.0.1 patient

## Run app (localhost:8080)

java -jar Patient-0.0.1.jar

## Containerize (Docker)

To build and run the app :
~~~
docker compose up --build
~~~

To stop the app :
~~~
docker stop p9_patient
~~~

To remove the image :
~~~
docker rmi -f p9_patient
~~~
# URL : HomePage

http://patient:8080/home



