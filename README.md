# Commit Character Counter Plugin

[![Build](https://github.com/ovitrif/commit-char-counter/actions/workflows/build.yml/badge.svg)](https://github.com/ovitrif/commit-char-counter/actions/workflows/build.yml)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A modern Kotlin-based IntelliJ IDEA/Android Studio plugin that displays a real-time character counter for commit messages.

## Features

- **Real-time counting** - See character count update as you type
- **Visual feedback** - Counter displays in `X/50` format
- **Clear indicators** - Text turns red when exceeding 50 characters
- **First-line focus** - Counts only the subject line (best practice for Git commits)
- **Inline display** - Shows directly in the commit message toolbar
- **Lightweight** - No configuration needed, works out of the box

## Why 50 Characters?

Following Git best practices, commit message subject lines should be 50 characters or less for optimal readability in git logs and GitHub interfaces.

## Installation

### From JetBrains Marketplace (Recommended)

1. Open **Settings/Preferences** → **Plugins** → **Marketplace**
2. Search for "Commit Character Counter"
3. Click **Install**
4. Restart the IDE

### Manual Installation from JAR

1. Download the latest release from [GitHub Releases](https://github.com/ovitrif/commit-char-counter/releases)

   Or build from source:
   ```bash
   ./gradlew build
   ```

2. The plugin ZIP will be located at:
   ```
   build/distributions/commit-char-counter-1.0.0.zip
   ```

3. Install in IntelliJ IDEA/Android Studio:
   - Open **Settings/Preferences** → **Plugins**
   - Click the gear icon ⚙️ → **Install Plugin from Disk...**
   - Select the downloaded/built ZIP file
   - Restart the IDE

## Usage

Once installed, the character counter will automatically appear in the commit dialog (VCS → Commit):

- The counter shows in the "Before Commit" section (bottom left area)
- Format: `0/50`, `32/50`, `83/50` (red when over 50)
- Updates in real-time as you type your commit message

## Development

### Prerequisites

- JDK 17 or later
- Gradle 8.10.2+ (included via wrapper)

### Building

```bash
./gradlew build
```

### Running in IDE Sandbox

```bash
./gradlew runIde
```

This will launch a sandboxed instance of IntelliJ IDEA with the plugin installed.

### Project Structure

```
.
├── src/main/kotlin/io/github/ovitrif/commitcharcounter/
│   ├── CommitCharCounterHandlerFactory.kt  # Factory for creating handlers
│   └── CommitCharCounterHandler.kt          # Main logic and UI
├── src/main/resources/META-INF/
│   └── plugin.xml                           # Plugin configuration
├── build.gradle.kts                         # Build configuration
└── settings.gradle.kts                      # Project settings
```

## Technical Details

- **Plugin ID**: `io.github.ovitrif.commit-char-counter`
- **Target Platform**: IntelliJ IDEA 2023.3+ / Android Studio 2023.3+
- **Build Compatibility**: 233 - 262.*
- **Kotlin Version**: 2.0.21
- **IntelliJ Platform Gradle Plugin**: 2.1.0

### How it Works

The plugin uses the IntelliJ Platform's `CheckinHandlerFactory` extension point to:
1. Hook into the VCS commit dialog
2. Find the commit message `EditorTextField` component
3. Attach a `DocumentListener` for real-time text changes
4. Display a `JBLabel` with the character count
5. Update the label color based on the 50-character threshold

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Changelog

See [CHANGELOG.md](CHANGELOG.md) for version history and release notes.

## Author

**ovitrif**
- GitHub: [@ovitrif](https://github.com/ovitrif)
- Email: ovitrif@proton.me

## Support

If you find this plugin helpful, please consider:
- ⭐ Starring the repository
- 🐛 Reporting issues on [GitHub Issues](https://github.com/ovitrif/commit-char-counter/issues)
- 💡 Suggesting new features or improvements
