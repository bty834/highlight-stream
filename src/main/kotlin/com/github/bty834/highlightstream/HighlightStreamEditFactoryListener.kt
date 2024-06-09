package com.github.bty834.highlightstream

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener



class HighlightStreamEditFactoryListener : EditorFactoryListener {

    override fun editorCreated(event: EditorFactoryEvent) {
        val editor: Editor = event.editor
        if (editor.project == null) {
            return
        }
        val caretListener = HighlightStreamCaretListener()
        editor2CaretListener[editor] = caretListener
        editor.caretModel.addCaretListener(caretListener)
    }

    override fun editorReleased(event: EditorFactoryEvent) {
        val editor = event.editor
        if (editor.project == null) {
            return
        }
        editor2CaretListener[editor]?.let { editor.caretModel.removeCaretListener(it) }
    }
}

