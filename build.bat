call mvnw clean install -DskipTests=true
pause
cd F:/Projets_OPC/P9_Notes
call mvnw clean install -DskipTests=true
pause
cd F:/Projets_OPC/P9_assessments
call mvnw clean install -DskipTests=true
pause
cd F:/Projets_OPC/P9_Patient
docker-compose down --rmi all
docker-compose up --build
pause