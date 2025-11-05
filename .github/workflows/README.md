# GitHub Actions Workflows for Banking Microservices

This directory contains automated workflows that integrate with Devin API for various development tasks.

## Available Workflows

### 1. Automated Code Review with Devin (`devin-code-review.yml`)

**Trigger**: Automatically runs on pull requests or can be manually triggered

**Purpose**: Creates a Devin session to review code changes for:
- Code quality and best practices
- Security vulnerabilities (authentication, financial transactions)
- Spring Boot and microservices patterns
- Database interactions and transaction handling
- API endpoint security and validation
- Test coverage and quality

**Usage**:
- Automatically triggered when PRs are opened or updated
- Can be manually triggered from Actions tab with custom review focus
- Posts a comment on the PR with the Devin session link

**Required Secrets**:
- `DEVIN_API_KEY`: Your Devin API key from [settings](https://app.devin.ai/settings)

**Example Manual Trigger**:
1. Go to Actions tab
2. Select "Automated Code Review with Devin"
3. Click "Run workflow"
4. Optionally specify a review focus area

### 2. Fix Failing Tests with Devin (`devin-test-fix.yml`)

**Trigger**: Manual workflow dispatch only

**Purpose**: Creates a Devin session to automatically fix failing tests in specified services

**Usage**:
1. Go to Actions tab
2. Select "Fix Failing Tests with Devin"
3. Click "Run workflow"
4. Choose which service to fix (or "all" for all services)
5. Optionally provide additional instructions
6. Devin will analyze failures, fix code, and verify the fixes

**Required Secrets**:
- `DEVIN_API_KEY`: Your Devin API key

**Services Available**:
- all (runs tests on testable services)
- Service-Registry
- Sequence-Generator
- API-Gateway
- User-Service
- Account-Service
- Fund-Transfer
- Transaction-Service

## Setup Instructions

**✅ Repository Status**: The DEVIN-GITHUB-ACTIONS repository is now PUBLIC, which allows these workflows to access the Devin API action.

### 1. Get a Devin API Key

1. Visit [Devin Settings](https://app.devin.ai/settings)
2. Navigate to API Keys section
3. Create a new API key
4. Copy the key (you won't be able to see it again)

### 2. Add API Key as GitHub Secret

1. Go to your repository on GitHub
2. Navigate to Settings → Secrets and variables → Actions
3. Click "New repository secret"
4. Name: `DEVIN_API_KEY`
5. Value: Paste your Devin API key
6. Click "Add secret"

### 3. Enable Workflows

The workflows are now ready to use! They will:
- Automatically trigger on pull requests (code review)
- Be available for manual triggering from the Actions tab

## Workflow Outputs

All workflows provide:
- **Session ID**: Unique identifier for the Devin session
- **Session URL**: Direct link to monitor Devin's progress
- **Session Status**: Information about the session state

You can monitor Devin's work in real-time by visiting the session URL provided in the workflow output.

## Customization

You can customize these workflows by:
- Modifying the `prompt` in the workflow files to change Devin's instructions
- Adding or removing `tags` for better session organization
- Adjusting `max-acu-limit` to control session resource usage
- Adding more workflows for other use cases (e.g., documentation generation, refactoring)

## Devin Action Documentation

For complete documentation on the Devin GitHub Action, see:
- [Action Repository](https://github.com/samfert-codeium/DEVIN-GITHUB-ACTIONS)
- [Usage Examples](https://github.com/samfert-codeium/DEVIN-GITHUB-ACTIONS/tree/main/devin-action/examples)
- [Devin API Documentation](https://docs.devin.ai/api-reference/overview)

## Troubleshooting

### Workflow Fails with Authentication Error
- Verify `DEVIN_API_KEY` secret is set correctly
- Check that your API key is still valid in Devin settings
- Ensure the key has necessary permissions

### Session Creation Fails
- Check Devin API status
- Verify your account has available ACU credits
- Review the workflow logs for specific error messages

### Unable to Access Session URL
- Ensure you're logged into Devin
- Verify you have access to the session (org permissions)
- Try refreshing the page or clearing browser cache

## Support

For issues with:
- **Workflows**: Open an issue in this repository
- **Devin Action**: Visit [DEVIN-GITHUB-ACTIONS Issues](https://github.com/samfert-codeium/DEVIN-GITHUB-ACTIONS/issues)
- **Devin API**: Contact [Devin Support](https://app.devin.ai/support)
