# Keycloak Configuration for Authentication Improvements

## Enable PKCE for Authorization Code Flow

1. Login to Keycloak Admin Console (http://localhost:8571)
2. Navigate to Realm Settings → banking-service
3. Go to Clients → banking-service-client
4. Enable the following settings:
   - **Client authentication**: ON
   - **Authorization**: ON
   - **Standard flow enabled**: ON
   - **Direct access grants enabled**: OFF (disable Password Grant)
   - **Proof Key for Code Exchange Code Challenge Method**: S256
5. Add Valid Redirect URIs: `http://localhost:8080/login/oauth2/code/keycloak`
6. Save changes

## Configure Roles for RBAC

1. In Keycloak Admin Console, navigate to Realm Roles
2. Create the following roles:
   - `ADMIN` - Full system access
   - `USER` - Regular user access
   - `SYSTEM` - Internal service-to-service communication

3. Assign default roles:
   - Navigate to Realm Settings → User Registration → Default Roles
   - Add `USER` as default role for new users

## Configure MFA (Multi-Factor Authentication)

1. Navigate to Realm Settings → Authentication
2. Go to Required Actions tab
3. Enable "Configure OTP" as a required action
4. For conditional MFA (high-value operations):
   - Create a new Authentication Flow
   - Name it "Conditional MFA Flow"
   - Add "Conditional OTP" step
   - Configure conditions based on client IP or step-up authentication

5. Update clients to use this flow for sensitive operations

## Client Scopes

Ensure the following scopes are configured:
- `openid` - Required
- `profile` - User profile information
- `email` - User email
- `offline_access` - Refresh tokens
- `roles` - Role information in JWT

## Token Settings

1. Navigate to Realm Settings → Tokens
2. Configure token lifespans:
   - **Access Token Lifespan**: 5 minutes
   - **Refresh Token Lifespan**: 30 minutes
   - **Refresh Token Max Reuse**: 0 (enable rotation)

## Testing

After configuration, test the Authorization Code Flow:

```bash
# Get authorization code (will redirect to Keycloak login)
curl -v "http://localhost:8571/realms/banking-service/protocol/openid-connect/auth?client_id=banking-service-client&response_type=code&scope=openid&redirect_uri=http://localhost:8080/login/oauth2/code/keycloak&code_challenge=CHALLENGE&code_challenge_method=S256"

# Exchange code for tokens
curl -X POST "http://localhost:8571/realms/banking-service/protocol/openid-connect/token" \
  -d "grant_type=authorization_code" \
  -d "client_id=banking-service-client" \
  -d "client_secret=YOUR_SECRET" \
  -d "code=AUTH_CODE" \
  -d "redirect_uri=http://localhost:8080/login/oauth2/code/keycloak" \
  -d "code_verifier=VERIFIER"
```

## Role Mapping

Ensure roles are included in JWT tokens:

1. Navigate to Clients → banking-service-client → Client Scopes
2. Click on "banking-service-client-dedicated"
3. Add mapper:
   - Name: realm-roles
   - Mapper Type: User Realm Role
   - Token Claim Name: realm_access.roles
   - Claim JSON Type: String
   - Add to ID token: ON
   - Add to access token: ON
   - Add to userinfo: ON

## Troubleshooting

If roles are not appearing in JWT:
1. Check that realm roles are created
2. Verify users have roles assigned
3. Ensure role mapper is configured correctly
4. Check token by decoding at jwt.io
