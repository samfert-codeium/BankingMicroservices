# Spring Boot Microservices Banking Application

A comprehensive banking application built using microservices architecture with Spring Boot, Spring Cloud, and Spring Security OAuth2 with Keycloak integration.

## Table of Contents

- [About](#about)
- [Architecture](#architecture)
- [Microservices](#microservices)
- [Project Structure](#project-structure)
- [Requirements](#requirements)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [API Examples](#api-examples)
- [Authentication](#authentication)
- [Future Enhancements](#future-enhancements)
- [Contributing](#contributing)

## About

The Banking Application is built using a microservices architecture, incorporating the Spring Boot framework along with other Spring technologies such as Spring Data JPA, Spring Cloud, and Spring Security. The application provides core banking functionalities including user management, account management, fund transfers, and transaction processing.

Key features include OAuth2/JWT authentication with Keycloak, service discovery with Eureka, centralized API routing through an API Gateway, and independent databases for each microservice ensuring data isolation and scalability.

## Architecture

The application follows a microservices architecture pattern with the following key components:

**Service Registry (Eureka)**: All microservices register themselves with the Eureka server for service discovery. This enables services to communicate with each other without hardcoding endpoints, providing dynamic service location and load balancing capabilities.

**API Gateway**: Serves as the single entry point for all client requests. It handles request routing to appropriate microservices, implements OAuth2 security with Keycloak integration, and provides centralized authentication and authorization.

**Database per Microservice**: Each microservice maintains its own MySQL database, ensuring data isolation, independent scaling, and loose coupling between services. This pattern allows each service to evolve its data schema independently.

**Feign Clients**: Inter-service communication is handled through declarative Feign clients with Eureka service discovery, enabling type-safe HTTP communication between microservices.

## Microservices

| Service | Port | Description |
|---------|------|-------------|
| Service-Registry | 8761 | Eureka server for service discovery and registration |
| API-Gateway | 8080 | Central entry point with OAuth2 security and request routing |
| User-Service | 8082 | User management, registration, and Keycloak integration |
| Account-Service | 8081 | Account creation, management, and balance operations |
| Fund-Transfer | 8083 | Fund transfer operations between accounts |
| Transaction-Service | 8084 | Transaction recording, history, deposits, and withdrawals |
| Sequence-Generator | 8085 | Generates unique sequence numbers for accounts and transactions |

### User Service

Manages user lifecycle including registration, profile updates, and authentication. Users are stored in both Keycloak (for credentials) and MySQL (for application data). New users require admin approval before activation.

### Account Service

Handles bank account operations including account creation, balance inquiries, account updates, and closure. Each account is linked to a user and maintains its own transaction history.

### Fund Transfer Service

Facilitates money transfers between accounts. Validates source and destination accounts, checks available balance, and creates corresponding transaction records.

### Transaction Service

Records all financial transactions including deposits, withdrawals, and transfers. Provides transaction history queries by account number or reference ID.

## Project Structure

```
BankingMicroservices/
|-- API-Gateway/                    # API Gateway service
|   +-- src/main/java/org/training/gateway/
|       |-- ApiGatewayApplication.java
|       +-- configuration/
|           +-- SecurityConfig.java
|-- Service-Registry/               # Eureka Server
|   +-- src/main/java/org/training/serviceregistry/
|       +-- ServiceRegistryApplication.java
|-- Sequence-Generator/             # Sequence generation service
|   +-- src/main/java/org/training/sequence/
|       |-- controller/
|       |-- service/
|       |-- repository/
|       +-- model/
|-- User-Service/                   # User management service
|   +-- src/main/java/org/training/user/
|       |-- controller/
|       |-- service/
|       |-- repository/
|       |-- model/
|       |-- external/
|       +-- exception/
|-- Account-Service/                # Account management service
|   +-- src/main/java/org/training/account/
|       |-- controller/
|       |-- service/
|       |-- repository/
|       |-- model/
|       |-- external/
|       +-- exception/
|-- Fund-Transfer/                  # Fund transfer service
|   +-- src/main/java/org/training/fundtransfer/
|       |-- controller/
|       |-- service/
|       |-- repository/
|       |-- model/
|       |-- external/
|       +-- exception/
+-- Transaction-Service/            # Transaction service
    +-- src/main/java/org/training/transactions/
        |-- controller/
        |-- service/
        |-- repository/
        |-- model/
        |-- external/
        +-- exception/
```

## Requirements

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Keycloak 21+ (for authentication)
- Docker (optional, for containerized deployment)

## Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/samfert-codeium/BankingMicroservices.git
   cd BankingMicroservices
   ```

2. **Set up MySQL databases**
   
   Create separate databases for each microservice:
   ```sql
   CREATE DATABASE user_service_db;
   CREATE DATABASE account_service_db;
   CREATE DATABASE fund_transfer_db;
   CREATE DATABASE transaction_service_db;
   CREATE DATABASE sequence_generator_db;
   ```

3. **Set up Keycloak**
   
   - Download and start Keycloak server on port 8571
   - Create a realm named `banking-service`
   - Create two clients:
     - `banking-service-client`: For OAuth2 user login (authorization_code grant)
     - `banking-service-api-client`: For admin operations (client_credentials grant)

4. **Build all microservices**
   ```bash
   mvn clean install
   ```

## Configuration

Each microservice has its own `application.yml` configuration file. Key configurations include:

**Database Configuration** (example for User-Service):
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/user_service_db
    username: your_username
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
```

**Eureka Client Configuration**:
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

**Keycloak Configuration** (API-Gateway):
```yaml
spring:
  security:
    oauth2:
      client:
        provider:
          keycloak:
            issuer-uri: http://localhost:8571/realms/banking-service
      resourceserver:
        jwt:
          jwk-set-uri: http://localhost:8571/realms/banking-service/protocol/openid-connect/certs
```

## Running the Application

Start the microservices in the following order:

1. **Start Service Registry** (must be first)
   ```bash
   cd Service-Registry
   mvn spring-boot:run
   ```
   Access Eureka dashboard at: http://localhost:8761

2. **Start Sequence Generator**
   ```bash
   cd Sequence-Generator
   mvn spring-boot:run
   ```

3. **Start User Service**
   ```bash
   cd User-Service
   mvn spring-boot:run
   ```

4. **Start Account Service**
   ```bash
   cd Account-Service
   mvn spring-boot:run
   ```

5. **Start Transaction Service**
   ```bash
   cd Transaction-Service
   mvn spring-boot:run
   ```

6. **Start Fund Transfer Service**
   ```bash
   cd Fund-Transfer
   mvn spring-boot:run
   ```

7. **Start API Gateway** (start last)
   ```bash
   cd API-Gateway
   mvn spring-boot:run
   ```

All API requests should go through the API Gateway at http://localhost:8080

## API Documentation

### User Service Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/users/register | Register a new user (public) |
| GET | /api/users/{id} | Get user by ID |
| GET | /api/users | Get all users |
| PATCH | /api/users/{id} | Update user status |

### Account Service Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/accounts | Create a new account |
| GET | /api/accounts | Get account by account number |
| GET | /api/accounts/user/{userId} | Get all accounts for a user |
| PUT | /api/accounts | Update account details |
| PATCH | /api/accounts | Update account status |

### Fund Transfer Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/fund-transfers | Initiate a fund transfer |
| GET | /api/fund-transfers/{referenceId} | Get transfer by reference ID |
| GET | /api/fund-transfers | Get all fund transfers |

### Transaction Service Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/transactions | Create a transaction |
| POST | /api/transactions/internal | Create internal transfer transaction |
| GET | /api/transactions/{referenceId} | Get transaction by reference ID |
| GET | /api/transactions/account/{accountId} | Get transactions by account |

## API Examples

### Register a New User

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "password": "SecurePass123!",
    "contactNo": "+1234567890"
  }'
```

Response:
```json
{
  "responseCode": "201",
  "message": "User registered successfully. Awaiting admin approval."
}
```

### Create an Account

```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your_jwt_token>" \
  -d '{
    "userId": 1,
    "accountType": "SAVINGS",
    "availableBalance": 1000.00
  }'
```

Response:
```json
{
  "accountId": 1,
  "accountNumber": "ACC1234567890",
  "accountType": "SAVINGS",
  "accountStatus": "ACTIVE",
  "availableBalance": 1000.00,
  "userId": 1
}
```

### Initiate a Fund Transfer

```bash
curl -X POST http://localhost:8080/api/fund-transfers \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your_jwt_token>" \
  -d '{
    "fromAccount": "ACC1234567890",
    "toAccount": "ACC0987654321",
    "amount": 250.00,
    "description": "Payment for services"
  }'
```

Response:
```json
{
  "transactionId": "TXN-2024-001",
  "message": "Fund transfer completed successfully"
}
```

### Make a Deposit

```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your_jwt_token>" \
  -d '{
    "accountId": "ACC1234567890",
    "transactionType": "DEPOSIT",
    "amount": 500.00,
    "description": "Cash deposit"
  }'
```

### Get Transaction History

```bash
curl -X GET "http://localhost:8080/api/transactions/account/ACC1234567890" \
  -H "Authorization: Bearer <your_jwt_token>"
```

Response:
```json
[
  {
    "referenceId": "TXN-2024-001",
    "accountId": "ACC1234567890",
    "transactionType": "DEPOSIT",
    "amount": 500.00,
    "localDateTime": "2024-01-15T10:30:00",
    "transactionStatus": "SUCCESS",
    "comments": "Cash deposit"
  }
]
```

## Authentication

The application uses Keycloak for OAuth2/JWT authentication. The authentication flow works as follows:

1. Users register through the public `/api/users/register` endpoint
2. New users are created in Keycloak with `enabled=false` and in MySQL with `status=PENDING`
3. An administrator must approve the user by updating their status to `APPROVED`
4. Upon approval, the user is enabled in Keycloak and can log in
5. Authenticated users receive a JWT token to access protected endpoints

### User Status Flow

```
PENDING -> APPROVED (user can login)
PENDING -> REJECTED (user cannot login)
APPROVED -> DISABLED (user temporarily blocked)
```

### Obtaining an Access Token

After admin approval, users can obtain a token through Keycloak:

```bash
curl -X POST "http://localhost:8571/realms/banking-service/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=banking-service-client" \
  -d "username=john.doe@example.com" \
  -d "password=SecurePass123!"
```

## Future Enhancements

- Notification system for transaction alerts via email and SMS
- Fixed deposits and recurring deposit functionality
- Investment portfolio management
- Mobile banking application
- Real-time transaction monitoring dashboard
- Multi-currency support
- Scheduled payments and standing orders

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

Please ensure your code follows the existing code style and includes appropriate Javadoc documentation.

## License

This project is available for educational and development purposes.

---

For detailed API documentation, visit our [API Documentation Portal](https://app.theneo.io/student/spring-boot-microservices-banking-application).

For Java documentation (Javadocs), see the [Java Documentation](https://kartik1502.github.io/Spring-Boot-Microservices-Banking-Application/)
