package io.github.ovitrif.commitcharcounter

import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.checkin.CheckinHandler
import com.intellij.openapi.vcs.ui.RefreshableOnComponent
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

class CommitCharCounterHandler(private val panel: CheckinProjectPanel) : CheckinHandler() {

    companion object {
        private const val CHAR_LIMIT = 50
    }

    override fun getBeforeCheckinConfigurationPanel(): RefreshableOnComponent {
        return object : RefreshableOnComponent {
            private val counterLabel = JBLabel("0/$CHAR_LIMIT").apply {
                border = JBUI.Borders.emptyRight(10)
                font = JBUI.Fonts.toolbarFont()
            }
            private val myPanel = JPanel(BorderLayout()).apply {
                add(counterLabel, BorderLayout.WEST)
            }

            init {
                setupDocumentListener()
                updateCounter()
            }

            override fun getComponent(): JComponent = myPanel

            override fun saveState() {
                // No state to save
            }

            override fun restoreState() {
                updateCounter()
            }

            private fun setupDocumentListener() {
                try {
                    val components = UIUtil.findComponentsOfType(
                        panel.component,
                        com.intellij.ui.EditorTextField::class.java
                    )
                    val commitMessageEditor = components.firstOrNull()

                    commitMessageEditor?.let { editor ->
                        val document = editor.document
                        val listener = object : DocumentListener {
                            override fun documentChanged(event: DocumentEvent) {
                                updateCounter()
                            }
                        }
                        document.addDocumentListener(listener)
                    }
                } catch (e: Exception) {
                    // Fallback: if we can't attach a listener, just show the initial count
                }
            }

            private fun updateCounter() {
                val text = try {
                    val components = UIUtil.findComponentsOfType(
                        panel.component,
                        com.intellij.ui.EditorTextField::class.java
                    )
                    components.firstOrNull()?.text ?: ""
                } catch (e: Exception) {
                    ""
                }
                // Only count the first line (subject line)
                val firstLine = text.lines().firstOrNull() ?: ""
                val length = firstLine.length
                counterLabel.text = "$length/$CHAR_LIMIT"

                // Change color to red if over limit
                counterLabel.foreground = if (length > CHAR_LIMIT) {
                    JBColor.RED
                } else {
                    UIUtil.getLabelForeground()
                }
            }
        }
    }
}
