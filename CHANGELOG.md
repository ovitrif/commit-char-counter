# Changelog

All notable changes to the "Commit Character Counter" plugin will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2025-01-18

### Added
- Real-time character counter for commit messages
- Visual feedback with X/50 format display
- Red color indicator when exceeding 50 characters
- First-line (subject line) character counting
- Inline display in commit message toolbar
- Support for IntelliJ IDEA 2023.3+ and Android Studio 2023.3+
- Two UI integration approaches:
  - CheckinHandlerFactory extension for legacy commit dialog
  - CustomComponentAction for modern VCS commit workflow

### Technical Details
- Built with Kotlin 2.0.21 and IntelliJ Platform Gradle Plugin 2.1.0
- Minimal dependencies (platform and VCS modules only)
- Compatible with build versions 233 to 262.*

[1.0.0]: https://github.com/ovitrif/commit-char-counter/releases/tag/v1.0.0
