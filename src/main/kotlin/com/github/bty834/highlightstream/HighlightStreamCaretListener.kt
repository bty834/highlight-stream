package com.github.bty834.highlightstream

import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.editor.markup.MarkupModel
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset


val editor2CaretListener: MutableMap<Editor, CaretListener> = HashMap()

val editor2RangeHighlighters: MutableMap<Editor, MutableList<RangeHighlighter>> = HashMap()

var even: Boolean = false

val attr: TextAttributesKey = TextAttributesKey.createTextAttributesKey("STREAM_LINE")

class HighlightStreamCaretListener : CaretListener {


    override fun caretPositionChanged(event: CaretEvent) {


        val editor: Editor = event.editor

        editor.project ?: return

        val markupModel: MarkupModel = editor.markupModel

        editor2RangeHighlighters[event.editor]?.forEach { markupModel.removeHighlighter(it) }

        val document: Document = editor.document

        val javaFile = PsiDocumentManager.getInstance(editor.project!!).getPsiFile(document)

        if (javaFile !is PsiJavaFile) {
            return
        }

        val currentEle: PsiElement = javaFile.findElementAt(editor.caretModel.offset) ?: return
        if (currentEle is PsiClass
            || currentEle is PsiCodeBlock
            || currentEle is PsiComment
            || currentEle is PsiModifier
            || currentEle is PsiAnnotation
            || currentEle is PsiMethod
            || currentEle is PsiJavaCodeReferenceElement
        ) {
            return
        }

        val collectParents: MutableList<PsiMethodCallExpression> =
            PsiTreeUtil.collectParents(currentEle, PsiMethodCallExpression::class.java, false, { it.parent.elementType?.equals(PsiClass::javaClass) == true })

        if (collectParents.isEmpty()) {
            return
        }

        collectParents.forEach { doHighlight(it, markupModel, editor) }


    }

    private fun doHighlight(psiMethodCallExpression: PsiMethodCallExpression, markupModel: MarkupModel, editor: Editor) {
        if (!psiMethodCallExpression.text.contains("stream")) {
            return
        }

        val methodCallList: MutableList<MethodCall> = ArrayList()

        findMethodCall(psiMethodCallExpression, methodCallList)

        methodCallList.removeIf { !it.isStreamMethod }



        methodCallList.forEach { methodCall ->
            val addRangeHighlighter = markupModel.addRangeHighlighter(
                attr,
                methodCall.startOffset,
                methodCall.endOffset,
                Integer.MAX_VALUE,
                HighlighterTargetArea.EXACT_RANGE
            )
            editor2RangeHighlighters.computeIfAbsent(editor) { ArrayList() }.add(addRangeHighlighter)
        }
    }

    private fun findMethodCall(psiMethodCallExpression: PsiMethodCallExpression, methodCallList: MutableList<MethodCall>) {

        val psiReferenceExpression = PsiTreeUtil.getChildOfType(psiMethodCallExpression, PsiReferenceExpression::class.java) ?: return

        val psiIdentifier = PsiTreeUtil.getChildOfType(psiReferenceExpression, PsiIdentifier::class.java)

        val methodName = psiIdentifier?.text ?: return

        val startOffset = psiIdentifier.startOffset
        val endOffset = psiIdentifier.endOffset
//        val psiExpressionList = PsiTreeUtil.getChildOfType(psiMethodCallExpression, PsiExpressionList::class.java)
//
//        val callText = psiExpressionList?.text ?: return

//        val endOffset = psiExpressionList.endOffset

        methodCallList.add(MethodCall(methodName, startOffset, endOffset))

        val nextMethodCall = PsiTreeUtil.getChildOfType(psiReferenceExpression, PsiMethodCallExpression::class.java) ?: return
        return findMethodCall(nextMethodCall, methodCallList);
    }
}