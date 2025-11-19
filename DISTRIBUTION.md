# Distribution Guide - JetBrains Marketplace

This guide walks you through publishing the Commit Character Counter plugin to the JetBrains Marketplace.

## Prerequisites

- [x] GitHub account
- [ ] JetBrains Account (free at [account.jetbrains.com](https://account.jetbrains.com))
- [ ] GitHub repository created at `github.com/ovitrif/commit-char-counter`

## Step 1: Create GitHub Repository

1. Go to [github.com/new](https://github.com/new)
2. Repository name: `commit-char-counter`
3. Description: "IntelliJ IDEA plugin that displays a real-time character counter for commit messages"
4. Make it public
5. Click **Create repository**

6. Push your code:
   ```bash
   cd /Users/ovitrif/repos/github/idea-commit-length
   git init
   git add .
   git commit -m "Initial commit - v1.0.0

   - Real-time character counter for commit messages
   - Visual feedback with X/50 format
   - Red color when exceeding 50 characters
   - Support for IntelliJ IDEA 2023.3+

   🤖 Generated with [Claude Code](https://claude.com/claude-code)

   Co-Authored-By: Claude <noreply@anthropic.com>"
   git branch -M main
   git remote add origin https://github.com/ovitrif/commit-char-counter.git
   git push -u origin main
   ```

## Step 2: Generate Plugin Signing Certificate

1. Go to [JetBrains Marketplace Hub](https://plugins.jetbrains.com/author/me)
2. Sign in with your JetBrains account
3. Navigate to **Marketplace** → **Organization** → **Plugin Signing**
4. Click **Generate New Certificate Chain**
5. Save the following values (you'll need them for GitHub secrets):
   - **Certificate Chain** (entire content including `-----BEGIN CERTIFICATE-----` and `-----END CERTIFICATE-----`)
   - **Private Key** (entire content including `-----BEGIN PRIVATE KEY-----` and `-----END PRIVATE KEY-----`)
   - **Private Key Password** (the password you set)

## Step 3: Generate Marketplace Publish Token

1. In JetBrains Marketplace Hub, go to your profile
2. Click **Get Permanent Token**
3. Or navigate to: [plugins.jetbrains.com/author/me/tokens](https://plugins.jetbrains.com/author/me/tokens)
4. Click **Generate Token**
5. Save this token securely (you'll need it for GitHub secrets)

## Step 4: Configure GitHub Repository Secrets

1. Go to your GitHub repository: `https://github.com/ovitrif/commit-char-counter`
2. Navigate to **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret** and add the following secrets:

| Secret Name | Value | Description |
|-------------|-------|-------------|
| `CERTIFICATE_CHAIN` | (Paste entire certificate chain) | Certificate from JetBrains Hub |
| `PRIVATE_KEY` | (Paste entire private key) | Private key from JetBrains Hub |
| `PRIVATE_KEY_PASSWORD` | (Paste password) | Password for the private key |
| `PUBLISH_TOKEN` | (Paste token) | Permanent token from JetBrains Marketplace |

### Important Notes:
- Include the `-----BEGIN/END-----` markers when pasting certificates and keys
- Keep these secrets secure - never commit them to the repository
- Each secret should be added individually

## Step 5: Capture Screenshots

Before publishing, capture screenshots for the marketplace listing:

1. Run the plugin in sandbox:
   ```bash
   ./gradlew runIde
   ```

2. Open a project with VCS enabled
3. Go to **VCS → Commit** (Cmd/Ctrl+K)
4. Capture these screenshots:
   - Normal counter display (e.g., "25/50")
   - Red counter when exceeded (e.g., "67/50")
   - Full commit dialog showing the counter location

5. Save screenshots to `docs/images/` directory:
   ```
   docs/images/
   ├── commit-counter-basic.png
   ├── commit-counter-exceeded.png
   └── commit-dialog-overview.png
   ```

## Step 6: Test the Build Locally

Before publishing, verify everything works:

1. Build the plugin:
   ```bash
   ./gradlew clean build
   ```

2. Test in sandbox:
   ```bash
   ./gradlew runIde
   ```

3. Verify the plugin:
   ```bash
   ./gradlew verifyPlugin
   ```

4. Test signing (requires secrets as environment variables):
   ```bash
   export CERTIFICATE_CHAIN="$(cat path/to/certificate-chain.txt)"
   export PRIVATE_KEY="$(cat path/to/private-key.txt)"
   export PRIVATE_KEY_PASSWORD="your-password"
   ./gradlew signPlugin
   ```

## Step 7: Create the First Release

### Option A: Manual Publishing (First Release)

1. Build and sign the plugin locally (see Step 6)

2. Go to [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/add)

3. Click **Upload Plugin**

4. Fill in the details:
   - **Plugin name**: Commit Character Counter
   - **Plugin ID**: `io.github.ovitrif.commit-char-counter`
   - **Category**: Version Control Systems
   - **License**: MIT License
   - **Source Code URL**: `https://github.com/ovitrif/commit-char-counter`
   - **Upload file**: Select `build/distributions/commit-char-counter-1.0.0.zip`

5. Add screenshots from `docs/images/`

6. Click **Publish**

7. Wait for JetBrains review (typically 1-3 business days)

### Option B: Automated Publishing via GitHub Release

Once GitHub secrets are configured and the plugin is initially published to the marketplace:

1. Create a Git tag:
   ```bash
   git tag -a v1.0.0 -m "Release v1.0.0"
   git push origin v1.0.0
   ```

2. Create a GitHub Release:
   - Go to `https://github.com/ovitrif/commit-char-counter/releases/new`
   - Tag: `v1.0.0`
   - Title: `v1.0.0 - Initial Release`
   - Description: Copy from `CHANGELOG.md`
   - Click **Publish release**

3. GitHub Actions will automatically:
   - Build the plugin
   - Sign the plugin
   - Publish to JetBrains Marketplace

## Step 8: Update README Badges

After the plugin is published, you'll receive a **Plugin ID** from JetBrains. Update the badges in `README.md`:

Replace `PLUGIN_ID` in these lines:
```markdown
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
```

With your actual plugin ID (e.g., `12345`):
```markdown
[![Version](https://img.shields.io/jetbrains/plugin/v/12345.svg)](https://plugins.jetbrains.com/plugin/12345)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/12345.svg)](https://plugins.jetbrains.com/plugin/12345)
```

## Publishing Future Updates

For subsequent releases:

1. Update version in `build.gradle.kts`:
   ```kotlin
   version = "1.1.0"
   ```

2. Update `CHANGELOG.md` with new changes

3. Update change notes in `plugin.xml`

4. Commit changes:
   ```bash
   git add .
   git commit -m "Bump version to 1.1.0"
   git push
   ```

5. Create and push tag:
   ```bash
   git tag -a v1.1.0 -m "Release v1.1.0"
   git push origin v1.1.0
   ```

6. Create GitHub Release - GitHub Actions will handle the rest!

## Troubleshooting

### Build Fails with "Plugin ID not found"
- Ensure `plugin.xml` has the correct `<id>` element
- Verify `build.gradle.kts` has matching configuration

### Signing Fails
- Check that all three secrets are set correctly in GitHub
- Ensure certificate chain and private key include BEGIN/END markers
- Verify the password matches the one set during certificate generation

### Publishing Fails with "401 Unauthorized"
- Regenerate the `PUBLISH_TOKEN` from JetBrains Hub
- Update the GitHub secret with the new token

### Plugin Rejected by JetBrains
- Common reasons:
  - Missing or inadequate description
  - No screenshots
  - License issues
  - Security vulnerabilities
- Address the feedback and resubmit

## Resources

- [JetBrains Plugin Development Documentation](https://plugins.jetbrains.com/docs/intellij/welcome.html)
- [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)
- [JetBrains Marketplace](https://plugins.jetbrains.com/)
- [Plugin Verification Tool](https://github.com/JetBrains/intellij-plugin-verifier)

## Support

If you encounter issues:
1. Check the [GitHub Issues](https://github.com/ovitrif/commit-char-counter/issues)
2. Review [JetBrains Plugin Development Forum](https://intellij-support.jetbrains.com/hc/en-us/community/topics/200366979-IntelliJ-IDEA-Open-API-and-Plugin-Development)
3. Contact: ovitrif@proton.me
