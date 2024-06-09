package com.github.bty834.highlightstream

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener

class HighlightStreamFinalize : ProjectManagerListener {

    override fun projectClosed(project: Project) {
        super.projectClosed(project)
        for (caretListener in editor2CaretListener) {
            caretListener.key.caretModel.removeCaretListener(caretListener.value);
        }
    }
}