# SonarQube Integration Setup

This document explains how to run SonarQube analysis on the Banking Microservices project.

## Prerequisites

- Maven installed
- SonarQube account with organization `samfert-codeium`
- SonarQube token for authentication

## Configuration

The SonarQube organization has been configured in all microservice `pom.xml` files:

```xml
<properties>
    <sonar.organization>samfert-codeium</sonar.organization>
</properties>
```

This applies to all 7 microservices:
- Account-Service
- API-Gateway
- Fund-Transfer
- Transaction-Service
- Sequence-Generator
- Service-Registry
- User-Service

## Running SonarQube Analysis

### 1. Set the SONAR_TOKEN Environment Variable

```bash
export SONAR_TOKEN=your_sonarqube_token_here
```

Replace `your_sonarqube_token_here` with your actual SonarQube authentication token.

### 2. Run the SonarQube Scanner

Execute the following command from the project root directory:

```bash
mvn verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=samfert-codeium_BankingMicroservices
```

Alternatively, you can use the shorthand version:

```bash
mvn verify sonar:sonar -Dsonar.projectKey=samfert-codeium_BankingMicroservices
```

### 3. Run Analysis for Individual Services

To analyze a specific microservice, navigate to its directory and run:

```bash
cd Account-Service
mvn verify sonar:sonar -Dsonar.projectKey=samfert-codeium_BankingMicroservices
```

## Notes

- The `verify` phase runs tests before the analysis
- Analysis results will be uploaded to SonarQube Cloud
- View results at: https://sonarcloud.io (after linking to your organization)
- The SONAR_TOKEN should be kept secure and not committed to version control

## CI/CD Integration

For automated SonarQube analysis in CI/CD pipelines, set the `SONAR_TOKEN` as a secret environment variable in your CI/CD platform and add the Maven command to your build pipeline.
