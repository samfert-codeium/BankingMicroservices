# Devin + SonarQube Automated Remediation Setup

This guide explains how to set up automated code remediation using Devin AI and SonarCloud.

## Demo Flow

```
1. Push broken/unsafe code to feature branch
2. Open a PR against main
3. GitHub Action triggers SonarCloud scan
4. SonarCloud Quality Gate fails (vulnerabilities, bugs, code smells)
5. Workflow triggers Devin AI session with detailed issue context
6. Devin analyzes and fixes the issues
7. Devin pushes fix with [devin-remediation] tag to the PR branch
8. Workflow re-runs, SonarCloud re-scans
9. Quality Gate passes ✅
```

## Key Features

- **Loop Prevention**: Detects Devin commits to avoid triggering infinite remediation loops
- **PR Comments**: Posts detailed status comments with links to Devin session
- **Comprehensive Context**: Passes all SonarQube issue details to Devin
- **Quality Gate Integration**: Uses official SonarQube Quality Gate action

## Prerequisites

1. **SonarCloud Account**: Sign up at [sonarcloud.io](https://sonarcloud.io)
2. **Devin API Access**: Get API key from [app.devin.ai](https://app.devin.ai)
3. **GitHub Repository**: With Actions enabled

## Setup Steps

### 1. Configure SonarCloud

1. Go to [sonarcloud.io](https://sonarcloud.io) and sign in with GitHub
2. Import your repository
3. Note down:
   - **Organization Key**: Found in Organization Settings
   - **Project Key**: Found in Project Information (bottom left)
4. Generate a token:
   - Click your profile icon → Security → Generate Tokens
   - Save the token securely

### 2. Configure GitHub Secrets

Go to your repository → Settings → Secrets and variables → Actions

Add these secrets:

| Secret Name | Description |
|-------------|-------------|
| `SONAR_TOKEN` | SonarCloud authentication token |
| `SONAR_ORG` | SonarCloud organization key (e.g., `samfert-codeium`) |
| `SONAR_PROJECT_KEY` | SonarCloud project key (e.g., `samfert-codeium_BankingMicroservices`) |
| `DEVIN_API_KEY` | Devin API key from app.devin.ai |

### 3. Grant Devin Repository Access

1. Go to [app.devin.ai](https://app.devin.ai)
2. Navigate to Settings → Integrations → GitHub
3. Grant Devin access to your repository
4. Ensure Devin has write permissions to push commits

### 4. Update sonar-project.properties

Edit `sonar-project.properties` in the repo root:

```properties
sonar.projectKey=YOUR_ORG_YOUR_REPO
sonar.organization=YOUR_ORG
```

## Running the Demo

### Step 1: Create a Feature Branch with Vulnerable Code

```bash
git checkout -b feature/demo-vulnerable-code
```

### Step 2: Add Vulnerable Code

The `demo/VulnerableCode.java` file contains intentional vulnerabilities:
- SQL Injection
- Hardcoded credentials
- Resource leaks
- Weak cryptography
- Null pointer dereference

### Step 3: Push and Create PR

```bash
git add .
git commit -m "feat: add new feature (contains issues for demo)"
git push origin feature/demo-vulnerable-code
```

Then create a PR on GitHub.

### Step 4: Watch the Magic

1. GitHub Action triggers automatically on PR
2. SonarCloud scans and Quality Gate fails
3. Workflow extracts all issue details from SonarCloud API
4. Devin session starts with full context
5. PR gets a comment with Devin session link
6. Devin pushes fix with `[devin-remediation]` in commit message
7. Workflow re-runs (detects Devin commit, skips new session)
8. Quality Gate passes ✅

## Workflow Architecture

### `.github/workflows/sonarcloud-devin.yml`

Single workflow that handles everything:

1. **SonarQube Scan** - Runs sonar-scanner CLI
2. **Quality Gate Check** - Uses official SonarQube action
3. **Devin Commit Detection** - Checks if commit is from Devin to prevent loops
4. **Issue Extraction** - Fetches detailed issues from SonarCloud API
5. **Devin Trigger** - Creates Devin session via API with full context
6. **PR Comment** - Posts status and links to PR

## Troubleshooting

### SonarCloud scan fails
- Verify `SONAR_TOKEN` is correct
- Check organization and project keys match
- Ensure `sonar-project.properties` exists

### Devin doesn't start
- Verify `DEVIN_API_KEY` is valid
- Ensure Devin has repository access
- Check workflow logs for API response

### Devin can't push
- Ensure Devin has write access to the repo
- Check branch protection rules allow Devin commits

### Infinite loop
- Devin commits should include `[devin-remediation]` in message
- Or commit author should be "Devin AI"
- Check the "Check if Devin Remediation Commit" step logs

## Reference Implementation

Based on: [mbatchelor81/inventory-demo](https://github.com/mbatchelor81/inventory-demo/actions/runs/19432099147/job/55593321665)

## Links

- [Devin Documentation](https://docs.devin.ai)
- [SonarCloud Documentation](https://docs.sonarcloud.io)
- [Devin SonarQube Guide](https://docs.devin.ai/enterprise/use-cases/sonarqube/guide)
