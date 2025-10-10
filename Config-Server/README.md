# Config Server

## 🚀 Introduction
The Config Server is a critical component in a microservices architecture, responsible for centralized configuration management. It allows microservices to externalize their configuration and retrieve it from a central location at runtime.

## 📚 Table of Contents
- [📂 Project Structure](#-project-structure)
- [🛠️ Technologies Used](#-technologies-used)
    - [🌱 Spring Boot](#spring-boot)
    - [☁️ Spring Cloud Config](#spring-cloud-config)
- [🔗 API Endpoints](#api-endpoints)
- [⚠️ Error Handling](#error-handling)
- [🔒 Security](#security)
- [⚙️ Configuration](#configuration)
- [📈 Monitoring](#monitoring)
- [📝 Logging](#logging)
- [🧪 Testing](#testing)
- [🚀 Build and Deployment](#build-and-deployment)

## 📂 Project Structure
The project structure of the Config Server is as follows:
```
Config-Server
├── src
│   ├── main
│   │   ├── java
│   │   │   └── org
│   │   │       └── training
│   │   │           └── config
│   │   │               └── server
│   │   │                   └── ConfigServerApplication.java
│   │   └── resources
│   │       ├── application.yml
│   │       └── config
│   │           ├── service-registry.yml
│   │           ├── api-gateway.yml
│   │           ├── user-service.yml
│   │           ├── account-service.yml
│   │           ├── fund-transfer-service.yml
│   │           ├── transaction-service.yml
│   │           └── sequence-generator.yml
├── .gitignore
├── Dockerfile
├── pom.xml
└── README.md
```

## 🛠️ Technologies Used

### 🌱 Spring Boot
Spring Boot is used to create stand-alone, production-grade Spring-based applications. It simplifies the configuration and deployment process by providing a set of default configurations and a wide range of features such as embedded servers, security, and monitoring.

### ☁️ Spring Cloud Config
Spring Cloud Config provides server and client-side support for externalized configuration in a distributed system. With the Config Server, you have a central place to manage external properties for applications across all environments.

## 🔗 API Endpoints
The Config Server exposes the following endpoints:

- `GET /{application}/{profile}[/{label}]` - Retrieve configuration for a specific application and profile
- `GET /{application}-{profile}.yml` - Retrieve configuration as YAML
- `GET /{application}-{profile}.properties` - Retrieve configuration as properties
- `GET /{label}/{application}-{profile}.yml` - Retrieve configuration from a specific label (branch/tag)

Example: `GET http://localhost:8888/user-service/default` retrieves the configuration for user-service.

## ⚠️ Error Handling
The Config Server uses standard HTTP status codes to indicate the success or failure of requests. Common status codes include:
- `200 OK` - The request was successful.
- `400 Bad Request` - The request was invalid or cannot be served.
- `404 Not Found` - The requested configuration was not found.
- `500 Internal Server Error` - An error occurred on the server.

## 🔒 Security
The Config Server can be secured using Spring Security. Authentication and authorization can be configured to protect the configuration endpoints and ensure only authorized services can access configurations.

## ⚙️ Configuration
The Config Server is configured to use the native profile, which stores configuration files in the classpath under `/config` directory. Each microservice has its own configuration file named `{service-name}.yml`.

### Centralized Configurations
The Config Server manages configurations for the following services:
- **Service Registry** (service-registry.yml) - Port 8761
- **API Gateway** (api-gateway.yml) - Port 8080
- **User Service** (user-service.yml) - Port 8082
- **Account Service** (account-service.yml) - Port 8081
- **Fund Transfer Service** (fund-transfer-service.yml) - Port 8085
- **Transaction Service** (transaction-service.yml) - Port 8084
- **Sequence Generator** (sequence-generator.yml) - Port 8083

### Environment Variable Support
All database configurations support environment variable substitution:
- `MYSQL_HOST` (default: localhost)
- `MYSQL_PORT` (default: 3306)
- `MYSQL_DB_NAME` (default: service-specific database name)
- `MYSQL_USER` (default: root)
- `MYSQL_PASSWORD` (default: root)

## 📈 Monitoring
Spring Boot Actuator is used for monitoring and managing the application. It provides various endpoints to check the health, metrics, and other operational information of the service.

## 📝 Logging
Logging is configured using Logback, which is the default logging framework in Spring Boot. It provides powerful and flexible logging capabilities.

## 🧪 Testing
JUnit and Mockito are used for unit and integration testing. These frameworks provide a comprehensive set of tools for writing and running tests.

## 🚀 Build and Deployment

### Building the Project
```bash
cd Config-Server
mvn clean install
```

### Running Locally
```bash
mvn spring-boot:run
```

The Config Server will start on port 8888.

### Startup Sequence
The recommended startup sequence for the microservices architecture is:
1. **Service Registry** (port 8761) - Must start first for service discovery
2. **Config Server** (port 8888) - Starts second to provide centralized configuration
3. **Other services** - Can start in any order, they will connect to Config Server via bootstrap.yml

### Docker Deployment
The service can be packaged as a Docker container for deployment in a containerized environment.

```bash
mvn clean package -DskipTests
docker build -t config-server .
docker run -p 8888:8888 config-server
```

## 📖 Usage by Client Services
Client services connect to the Config Server by adding the `spring-cloud-starter-config` dependency and creating a `bootstrap.yml` file with the Config Server URL:

```yaml
spring:
  application:
    name: {service-name}
  cloud:
    config:
      uri: http://localhost:8888
```

Client services will automatically fetch their configuration from the Config Server on startup based on their application name.
