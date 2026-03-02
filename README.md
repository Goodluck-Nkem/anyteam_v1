### Organization

#### entity:
- Defines a table via ORM, its integrity constraints and relationships.

#### request_dto:
- Data Request record for storing request data.
- This record is loaded from the deserialized HTTP request body.
- It also defines validation constraints which can throw MethodArgumentNotValidException.

#### response_dto:
- Data Response record for storing mapped entity fields.
- This record would be serialized and returned to client.
- It is filtered by the mapper to contain specific *entity* fields.

#### error_dto:
- Error Response record for storing the error message after an exception.
- This record would be serialized and returned to client.

#### mapper:
- Maps specific *entity* fields into the *response_dto*
- This ensures clients only sees what server wants them to see.

#### service:
- Derives required *entity* fields from the *request_dto*.
- Talks directly to the persistence layer on behalf of the controller.

#### repository:
- Implements the persistence layer using JPA, HQL, SQL.

#### controller: 
- Intercepts API requests. 
- Deserializes request body to *request_dto* with validations checks.
- Passes the valid *request_dto* to the service layer for processing.

#### exception:
- Handles exceptions from validations and requests.
- Sets error_dto's error message.

##
### Build and Run App
#### command_prompt:
    .\mvnw clean package -DskipTests && docker compose up --build
#### bash or command_prompt:
    ./mvnw.cmd clean package -DskipTests && docker compose up --build

##
### Stop App
#### keep-volumes:
    docker compose down
#### discard-volumes:
    docker compose down -v
