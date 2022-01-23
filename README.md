# Account Statement Service

**Components Used:**
1. Spring Boot : Core Framework
2. Spring Security : For Role based authentication/authorization along with JWT Token
3. Spring JDBC : To Query MS Access Database
4. Spring Redis : Cached DB to store Token generated (purpose to invalidate)

**Server Port :** 9080
**Redis Port:** 6379

**Pre-requisites:** Install and Run Redis service on machine

Steps to run the service after checking out project:
1. mvn clean install : This will generate a JAR 'account-statement-service-1.0.0-SNAPSHOT'. Test cases will be executed too.
2. mvn spring-boot:run : This will bring up service on server port(9080)

Swagger Documentation can be accessed via
http://localhost:9080/swagger-ui/index.html

**Authentication Service : Login Controller**
    This service is to authenticate the user based on 'username' and 'password'. Generates a token with validity configured.

**Account Statement Details : Statement Controller**
    This service is to fetch account statement for given parameters

**Attachments:**
1. Unit Test Coverage snapshots
2. Sonarqube Dashboard snapshots
3. Postman test snapshots

`**curl --location --request POST 'http://localhost:9080/authenticate' \
--header 'Content-Type: application/json' \
--data-raw '{
"username":"admin",
"password":"admin"
}'**`

`curl --location --request GET 'http://localhost:9080/account/statement/3?fromDate=22.11.2010&toDate=23.01.2022' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY0Mjk3MTU3NSwiaWF0IjoxNjQyOTcxMjc1fQ.S-RwDh1wkImclU-YH-fN7RD1Yut0prkxRXsFeiUW9_PnpRcG-jgdBHGVUfswNPlPYpu2C2HRbFssyvgxaN6HRg' \
--header 'Content-Type: application/json' \
--data-raw '{
"username":"admin",
"password":"admin"
}'
`

`curl --location --request POST 'http://localhost:9080/user/logout' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY0Mjk3MTU3NSwiaWF0IjoxNjQyOTcxMjc1fQ.S-RwDh1wkImclU-YH-fN7RD1Yut0prkxRXsFeiUW9_PnpRcG-jgdBHGVUfswNPlPYpu2C2HRbFssyvgxaN6HRg'
`
