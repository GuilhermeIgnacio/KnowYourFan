package com.guilherme.knowyourfan.core.presentation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CpfVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.filter { it.isDigit() }
        val formatted = when (trimmed.length) {
            in 0..3 -> trimmed
            in 4..6 -> "${trimmed.take(3)}.${trimmed.drop(3)}"
            in 7..9 -> "${trimmed.take(3)}.${trimmed.drop(3).take(3)}.${trimmed.drop(6)}"
            else -> "${trimmed.take(3)}.${trimmed.drop(3).take(3)}.${trimmed.drop(6).take(3)}-${trimmed.drop(9)}"
        }
        val annotated = AnnotatedString(formatted)

        val offsetMapping = object : OffsetMapping {
            override fun transformedToOriginal(offset: Int): Int {
                val textBefore = annotated.text.take(offset)
                return textBefore.count { it.isDigit() }
            }

            override fun originalToTransformed(offset: Int): Int {
                var count = 0
                var i = 0
                while (i < annotated.text.length && count < offset) {
                    if (annotated.text[i].isDigit()) {
                        count++
                    }
                    i++
                }
                return i
            }
        }

        return TransformedText(annotated, offsetMapping)
    }
}