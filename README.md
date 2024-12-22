# qp-assessment
Java Version: 17
DB Used: Postgres

# Postman collection
Postman collection is uploaded with the code and some samples

# Maven Build
Use the below command to build the project and to extract jar
mvn clean install package

# Docker build

Ensure the maven build is done and Postgres credentials used in docker-compose.yml are correct.
Build and Run using following command

docker-compose up --build
