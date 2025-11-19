# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

An IntelliJ IDEA/Android Studio plugin that displays a real-time character counter for commit messages. Shows count in `X/50` format with red color indication when exceeding 50 characters.

**Plugin ID**: `io.github.ovitrif.commit-char-counter`
**Target Platforms**: IntelliJ IDEA 2023.3+ / Android Studio 2023.3+
**Build Compatibility**: 233 - 262.*

## Build Commands

```bash
# Build the plugin (JAR output: build/libs/commit-char-counter-1.0.0.jar)
./gradlew build

# Run in IDE sandbox (launches IntelliJ IDEA with plugin installed)
./gradlew runIde

# Clean build artifacts
./gradlew clean
```

## Architecture

### Plugin Extension Points

The plugin uses two approaches to display the character counter:

1. **CheckinHandlerFactory Extension** (legacy approach)
   - Extension point: `com.intellij.checkinHandlerFactory`
   - Implementation: `CommitCharCounterHandlerFactory` → `CommitCharCounterHandler`
   - Displays counter in "Before Commit" section (bottom left of commit dialog)
   - Uses `RefreshableOnComponent` to inject custom UI

2. **Action-based Approach** (modern approach)
   - Action added to: `Vcs.MessageActionGroup` (inline with commit message)
   - Implementation: `CharCounterAction` implements `CustomComponentAction`
   - Uses `VcsDataKeys.COMMIT_WORKFLOW_HANDLER` to access commit message
   - Updates in real-time via action update events

### Key Implementation Details

**Commit Message Access**: Both implementations access the commit message through different mechanisms:
- `CommitCharCounterHandler`: Uses `UIUtil.findComponentsOfType()` to locate `EditorTextField` components and attach `DocumentListener`
- `CharCounterAction`: Retrieves via `AbstractCommitWorkflowHandler.getCommitMessage()` with fallback to UI component search

**Character Counting**: Only counts the **first line** (subject line) of the commit message using `text.lines().firstOrNull()`. This is important - multi-line commit messages only count the subject line.

**Color Indication**: Uses `JBColor.RED` when `length > 50`, otherwise uses `UIUtil.getLabelForeground()` for theme-appropriate default color.

**Plugin Dependencies**:
- `com.intellij.modules.platform` - Core platform APIs
- `com.intellij.modules.vcs` - VCS integration
- `Git4Idea` - Bundled Git plugin (required for VCS commit UI)

## Development Environment

- **JDK**: 17 (JVM toolchain configured)
- **Kotlin**: 2.0.21
- **Gradle**: 8.10.2+ via wrapper
- **IntelliJ Platform Gradle Plugin**: 2.1.0
- **Base IDE**: IntelliJ IDEA Community 2023.3

## Plugin Configuration

Configuration is split between `build.gradle.kts` and `src/main/resources/META-INF/plugin.xml`:
- `build.gradle.kts`: Build version, compatibility ranges, signing/publishing setup
- `plugin.xml`: Extension points, actions, dependencies, metadata

Note: `buildSearchableOptions` task is disabled in Gradle configuration.

## Testing

The plugin can be tested by running `./gradlew runIde` and opening the VCS commit dialog (VCS → Commit). Type in the commit message field to verify real-time counter updates and color changes.
