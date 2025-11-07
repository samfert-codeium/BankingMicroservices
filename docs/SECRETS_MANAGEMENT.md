# Secrets Management Guide

## Environment Variables

This application uses environment variables for sensitive configuration. Never commit secrets to version control.

## Required Environment Variables

Copy `.env.example` to `.env` and fill in your values:

```bash
cp .env.example .env
```

## Setting Environment Variables

**Linux/Mac:**
```bash
export KEYCLOAK_CLIENT_SECRET=your_secret_here
export KEYCLOAK_CLIENT_SECRET_API=your_api_secret_here
```

**Windows:**
```cmd
set KEYCLOAK_CLIENT_SECRET=your_secret_here
set KEYCLOAK_CLIENT_SECRET_API=your_api_secret_here
```

## Docker Compose

If using Docker Compose, create a `.env` file in the project root:

```yaml
# docker-compose.yml
services:
  api-gateway:
    environment:
      - KEYCLOAK_CLIENT_SECRET=${KEYCLOAK_CLIENT_SECRET}
```

## Spring Cloud Config (Advanced)

For production deployments, consider using Spring Cloud Config Server:

1. Set up Config Server
2. Store encrypted secrets in Git repository
3. Configure services to pull configuration from Config Server

Example configuration:
```yaml
spring:
  cloud:
    config:
      uri: http://config-server:8888
      fail-fast: true
```

## HashiCorp Vault (Advanced)

For enterprise deployments:

1. Install and configure Vault
2. Add Spring Vault dependencies to pom.xml:
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-vault-config</artifactId>
</dependency>
```

3. Configure Vault integration in bootstrap.yml:
```yaml
spring:
  cloud:
    vault:
      uri: http://localhost:8200
      token: ${VAULT_TOKEN}
      kv:
        enabled: true
```

4. Store secrets in Vault:
```bash
vault kv put secret/banking-microservices keycloak.client.secret=YOUR_SECRET
```

## AWS Secrets Manager (Advanced)

For AWS deployments:

1. Store secrets in AWS Secrets Manager
2. Add spring-cloud-aws-secrets-manager-config dependency
3. Configure IAM roles for access
4. Reference secrets in application.yml:
```yaml
spring:
  cloud:
    aws:
      secretsmanager:
        enabled: true
        region: us-east-1
```

## Security Best Practices

1. **Never commit secrets to version control**
   - Add `.env` to `.gitignore`
   - Use environment variables or secret management systems

2. **Rotate secrets regularly**
   - Change passwords and tokens periodically
   - Update all environments when rotating

3. **Use different secrets for different environments**
   - Dev, staging, and production should have different secrets
   - Never use production secrets in development

4. **Limit access to secrets**
   - Only authorized personnel should access production secrets
   - Use role-based access control for secret management systems

5. **Audit secret access**
   - Monitor who accesses secrets and when
   - Set up alerts for unauthorized access attempts
