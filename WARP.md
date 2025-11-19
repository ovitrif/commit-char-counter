# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Project Overview

A Kotlin-based IntelliJ IDEA/Android Studio plugin that displays a real-time character counter for commit messages. The counter shows in `X/50` format and turns red when exceeding 50 characters (counting only the first line of the commit message).

**Plugin ID**: `io.github.ovitrif.commit-char-counter`  
**Target Platform**: IntelliJ IDEA 2023.3+ / Android Studio 2023.3+ (build 233-262.*)  
**Build Tool**: Gradle 8.10.2+ with Kotlin DSL

## Common Development Commands

### Build
```bash
./gradlew build
```
Output JAR: `build/libs/commit-char-counter-1.0.0.jar`

### Run in IDE Sandbox
```bash
./gradlew runIde
```
Launches a sandboxed IntelliJ IDEA instance with the plugin installed for testing.

### Clean Build
```bash
./gradlew clean build
```

## Architecture

### Extension Points & Integration Strategy

The plugin uses **two complementary approaches** to display the commit message character counter:

1. **CheckinHandlerFactory Extension** (`CommitCharCounterHandlerFactory` → `CommitCharCounterHandler`)
   - Hooks into the VCS commit dialog via `com.intellij.checkinHandlerFactory` extension point
   - Creates a `RefreshableOnComponent` panel displayed in the "Before Commit" section
   - Uses component search to find the commit message `EditorTextField` and attaches a `DocumentListener` for real-time updates
   - Provides the primary UI in the commit dialog's bottom section

2. **Custom Action Component** (`CharCounterAction`)
   - Registered in `Vcs.MessageActionGroup` action group
   - Implements `CustomComponentAction` to display as inline UI element
   - Uses `VcsDataKeys.COMMIT_WORKFLOW_HANDLER` to access commit message
   - Falls back to component search if handler unavailable
   - Updates via action system's `update()` method

### Core Components

**`CommitCharCounterHandlerFactory.kt`**
- Factory that creates handler instances for each commit dialog
- Simple delegation to `CommitCharCounterHandler`

**`CommitCharCounterHandler.kt`**
- Creates the character counter UI component (`JBLabel`)
- Searches UI tree for `EditorTextField` containing commit message
- Attaches `DocumentListener` for real-time text change detection
- Extracts only first line of commit message for counting
- Updates label color: normal when ≤50 chars, `JBColor.RED` when >50

**`CharCounterAction.kt`**
- Alternative/complementary display mechanism using action system
- Tracks `isOverLimit` state for color management
- Provides `createCustomComponent()` and `updateCustomComponent()` for UI
- Implements dual text retrieval strategy via workflow handler and component search

**`plugin.xml`**
- Declares extension point: `com.intellij.checkinHandlerFactory`
- Registers action in `Vcs.MessageActionGroup`
- Depends on: `com.intellij.modules.platform` and `com.intellij.modules.vcs`

### Key Technical Details

- **Character limit**: Hardcoded to 50 in both components (`CHAR_LIMIT` constant)
- **Line handling**: Only the first line of multi-line commit messages is counted
- **UI framework**: Uses IntelliJ Platform's Swing-based UI (JBLabel, JBColor, JBUI for borders)
- **Component discovery**: Uses `UIUtil.findComponentsOfType()` to locate `EditorTextField`
- **Error handling**: Both components gracefully fall back to empty string on exceptions
- **Kotlin toolchain**: JVM target 17

## Development Guidelines

### When Modifying the Character Limit
Update the `CHAR_LIMIT` constant in both:
- `CommitCharCounterHandler.kt` (line 21)
- `CharCounterAction.kt` (line 17)

### Plugin Configuration Changes
- Version, description, and metadata: Edit `build.gradle.kts` under `intellijPlatform.pluginConfiguration`
- Extension points and actions: Edit `src/main/resources/META-INF/plugin.xml`

### Testing Changes
Always test in sandbox IDE using `./gradlew runIde` before building final JAR. The plugin requires:
- Opening VCS → Commit dialog
- Typing in commit message field to verify real-time updates
- Testing with messages above and below 50 characters
- Testing multi-line messages (only first line should be counted)

### Build Configuration Notes
- `buildSearchableOptions` task is disabled (line 55-57 in build.gradle.kts)
- Plugin signing and publishing use environment variables: `CERTIFICATE_CHAIN`, `PRIVATE_KEY`, `PRIVATE_KEY_PASSWORD`, `PUBLISH_TOKEN`
- Depends on bundled `Git4Idea` plugin for VCS integration
