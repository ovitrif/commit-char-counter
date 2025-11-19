# Screenshots for JetBrains Marketplace

This directory contains screenshots for the JetBrains Marketplace listing.

## Required Screenshots

To complete the marketplace listing, capture the following screenshots:

1. **commit-counter-basic.png** - Show the commit dialog with the character counter displaying a normal count (e.g., "25/50" in default color)

2. **commit-counter-exceeded.png** - Show the character counter in red when exceeding 50 characters (e.g., "67/50" in red)

3. **commit-counter-realtime.png** (optional) - Animated GIF or screenshot showing the counter updating in real-time as you type

4. **commit-dialog-overview.png** (optional) - Full commit dialog view showing where the counter appears in the UI

## How to Capture Screenshots

1. Run the plugin in a sandbox IDE:
   ```bash
   ./gradlew runIde
   ```

2. Open any project with VCS (Git) enabled

3. Go to **VCS → Commit** or use **Cmd/Ctrl+K**

4. Type in the commit message field to see the counter in action

5. Take screenshots:
   - Use macOS Screenshot (Cmd+Shift+4) or Windows Snipping Tool
   - Save as PNG format
   - Recommended size: 1280x800 or similar 16:10 ratio
   - Ensure good visibility of the counter

## Uploading to Marketplace

When creating/editing the plugin listing on JetBrains Marketplace:
1. Go to the plugin page editor
2. Navigate to the "Screenshots" section
3. Upload 2-4 images from this directory
4. Add descriptive captions for each screenshot
