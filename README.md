## MEDISCREEN APP : P9-Patient

Micro-service Patient manage patients informations on MEDISCREEN Application.

# Getting Started

# Prerequisite to run

- Java 1.8 JDK
- Spring Boot 2.4.4
- Docker 

# Installation

## Modifie host file :

- 127.0.0.1 patient
- 127.0.0.1 note
- 127.0.0.1 assessment

## Run app (localhost:8080)

java -jar Patient-0.0.1.jar

## Containerize (Docker)

To build and run the app (with Docker Compose):
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
# URL
## HOME PAGE
- http://patient:8080/home

# Architecture
![Alt Text](/Archi.png)


