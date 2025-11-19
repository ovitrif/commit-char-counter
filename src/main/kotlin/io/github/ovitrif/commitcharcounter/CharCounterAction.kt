package io.github.ovitrif.commitcharcounter

import com.intellij.ide.ActivityTracker
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ex.CustomComponentAction
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import com.intellij.vcs.commit.AbstractCommitWorkflowHandler
import javax.swing.JComponent
import javax.swing.SwingUtilities

class CharCounterAction : AnAction(), CustomComponentAction {

    companion object {
        private const val CHAR_LIMIT = 50
    }

    private var isOverLimit = false
    private var listenerAttached = false
    private var labelRef: JBLabel? = null

    override fun createCustomComponent(presentation: com.intellij.openapi.actionSystem.Presentation, place: String): JComponent {
        return JBLabel("0/$CHAR_LIMIT").apply {
            border = JBUI.Borders.empty(0, 8, 0, 8)
            font = JBUI.Fonts.toolbarFont()
            labelRef = this

            // Attach document listener for real-time updates
            SwingUtilities.invokeLater {
                attachDocumentListener(this)
            }
        }
    }

    private fun attachDocumentListener(label: JBLabel) {
        if (listenerAttached) return

        try {
            // Find the root component to search from
            val focusOwner = java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().focusOwner
            val rootPane = if (focusOwner != null) {
                javax.swing.SwingUtilities.getRoot(focusOwner) as? javax.swing.JComponent
            } else {
                null
            } ?: return

            val editors = UIUtil.findComponentsOfType(rootPane, com.intellij.ui.EditorTextField::class.java)
            val commitMessageEditor = editors.firstOrNull() ?: return

            commitMessageEditor.document.addDocumentListener(object : DocumentListener {
                override fun documentChanged(event: DocumentEvent) {
                    // Update label directly for instant feedback
                    val text = commitMessageEditor.text
                    val firstLine = text.lines().firstOrNull() ?: ""
                    val length = firstLine.length

                    SwingUtilities.invokeLater {
                        label.text = "$length/$CHAR_LIMIT"
                        label.foreground = if (length > CHAR_LIMIT) {
                            JBColor.RED
                        } else {
                            UIUtil.getLabelForeground()
                        }
                    }
                }
            })

            listenerAttached = true
        } catch (e: Exception) {
            // Fallback to regular action updates if listener attachment fails
        }
    }

    override fun updateCustomComponent(component: JComponent, presentation: com.intellij.openapi.actionSystem.Presentation) {
        if (component is JBLabel) {
            component.text = presentation.text
            component.foreground = if (isOverLimit) {
                JBColor.RED
            } else {
                UIUtil.getLabelForeground()
            }
        }
    }

    override fun update(e: AnActionEvent) {
        val handler = e.getData(VcsDataKeys.COMMIT_WORKFLOW_HANDLER)

        if (handler is AbstractCommitWorkflowHandler<*, *>) {
            val text = try {
                handler.getCommitMessage()
            } catch (ex: Exception) {
                ""
            }

            val firstLine = getFirstLine(text)
            val length = firstLine.length
            isOverLimit = length > CHAR_LIMIT
            e.presentation.text = "$length/$CHAR_LIMIT"
            e.presentation.isEnabled = true
            e.presentation.isVisible = true
        } else {
            // Fallback: try to get text from UI component search
            val text = getCommitMessageFromComponents(e) ?: ""
            val firstLine = getFirstLine(text)
            val length = firstLine.length
            isOverLimit = length > CHAR_LIMIT
            e.presentation.text = "$length/$CHAR_LIMIT"
            e.presentation.isEnabled = true
            e.presentation.isVisible = true
        }
    }

    private fun getFirstLine(text: String): String {
        return text.lines().firstOrNull() ?: ""
    }

    private fun getCommitMessageFromComponents(e: AnActionEvent): String? {
        return try {
            e.getData(com.intellij.openapi.actionSystem.PlatformDataKeys.CONTEXT_COMPONENT)?.let { component ->
                val editors = UIUtil.findComponentsOfType(
                    UIUtil.getRootPane(component),
                    com.intellij.ui.EditorTextField::class.java
                )
                editors.firstOrNull()?.text
            }
        } catch (ex: Exception) {
            null
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        // Action is display-only, no action on click
    }
}
