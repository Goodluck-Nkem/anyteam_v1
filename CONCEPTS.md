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

#### projection:
- Joined columns definitions for repository.
- This helps avoid returning individual entities in separate queries.
- Instead, allows getting required fields in one trip

#### mapper:
- Maps specific *entity* or *projection* fields into the *response_dto*
- This ensures clients only sees what server wants them to see.

#### service:
- Derives required *entity* fields from the *request_dto*.
- Talks directly to the persistence layer on behalf of the controller.
- Handles system logic and security helpers

#### repository:
- Implements the persistence layer using JPA, HQL, SQL.

#### config:
- Handles actions required before system startup like initializers, security, etc

#### controller:
- Intercepts API requests.
- Deserializes request body to *request_dto* with validations checks.
- Passes the valid *request_dto* to the service layer for processing.

#### exception:
- Handles exceptions from validations and requests.
- Sets error_dto's error message.

##