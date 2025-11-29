# Plugin Screenshots

This directory contains screenshots for the JetBrains Marketplace listing.

## Current Screenshots

| File | Description |
|------|-------------|
| `screenshot.png` | **Primary screenshot** - Commit dialog showing the character counter in action |

## Adding More Screenshots

Future screenshots to consider:

- **exceeded.png** - Counter in red when exceeding 50 characters (e.g., "67/50")
- **realtime.gif** - Animated GIF showing counter updating while typing

### How to Capture

1. Run the plugin in a sandbox IDE:
   ```bash
   ./gradlew runIde
   ```
2. Open any project with Git enabled
3. Open commit dialog (**Cmd/Ctrl+K**)
4. Capture screenshot (macOS: Cmd+Shift+4)
5. Save as PNG, recommended size: 1280x800 or 16:10 ratio
