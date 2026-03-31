##### See CONCEPTS.md and DESCRIPTION.md for more details
- DESCRIPTION.md explains what the anyteam project is all about.
- CONCEPTS.md shows how I organized layers of the app

### Build and Run App
#### clean build + docker run + skip tests:
    ./mvnw clean package -DskipTests && docker compose up --build

#### clean test (preserve color formating):
    ./mvnw clean test -Dstyle.color=always

#### run without building jar (apply application-use_h2.properties)
    ./mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=use_h2

#### clean build jar
    mvn clean package

#### run via jar (using hardcoded postgres running locally)
    java -jar APP.jar --spring.datasource.url=jdbc:postgresql://localhost:5432/anyteam_v1_db

##
### Stop App
#### keep-volumes:
    docker compose down
#### discard-volumes:
    docker compose down -v
