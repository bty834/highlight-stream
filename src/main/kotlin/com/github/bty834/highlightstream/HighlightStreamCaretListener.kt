package com.github.bty834.highlightstream

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.editor.markup.MarkupModel
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.ui.JBColor

val editor2CaretListener: MutableMap<Editor, CaretListener> = HashMap()

val editor2RangeHighlighters: MutableMap<Editor, MutableList<RangeHighlighter>> = HashMap()

class HighlightStreamCaretListener : CaretListener {


    override fun caretPositionChanged(event: CaretEvent) {


        val editor: Editor = event.editor

        val curOffset = editor.caretModel.offset

        val start = Math.max(curOffset - 10, 0)

        val markupModel: MarkupModel = editor.markupModel

        editor2RangeHighlighters[event.editor]?.forEach { markupModel.removeHighlighter(it)}

        val attrKey: TextAttributesKey =  TextAttributesKey.createTextAttributesKey("STREAM_LINE_ODD")


        val addRangeHighlighter = markupModel.addRangeHighlighter(attrKey, start, curOffset + 10, Integer.MAX_VALUE, HighlighterTargetArea.EXACT_RANGE)
        editor2RangeHighlighters.computeIfAbsent(editor) { ArrayList() }.add(addRangeHighlighter)

    }
}