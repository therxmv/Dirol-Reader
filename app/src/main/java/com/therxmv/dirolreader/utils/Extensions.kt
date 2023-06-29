package com.therxmv.dirolreader.utils

import android.util.Log
import org.drinkless.td.libcore.telegram.TdApi

fun TdApi.FormattedText.toMarkdown(): String {
    var result = this.text

    this.entities.forEach {
        val startIndex = it.offset
        val endIndex = startIndex + it.length
        var subText = this.text.subSequence(startIndex, endIndex).toString()

        if(subText.last() == ' ') {
            subText = subText.dropLast(1)
        }

        when (it.type) {
            is TdApi.TextEntityTypeBold -> {
                result = result.replace(subText, "**$subText**")
            }

            is TdApi.TextEntityTypeItalic -> {
                result = result.replace(subText, "_${subText}_")
            }

            is TdApi.TextEntityTypeStrikethrough -> {
                result = result.replace(subText, "~~$subText~~")
            }

            is TdApi.TextEntityTypeUnderline -> {
                result = result.replace(subText, "__${subText}__")
            }

            is TdApi.TextEntityTypeCode -> {
                result = result.replace(subText, "`$subText`")
            }

            is TdApi.TextEntityTypePre -> {
                result = result.replace(subText, "```$subText```")
            }

            is TdApi.TextEntityTypeUrl -> {
                result = result.replace(subText, "[$subText]($subText)")
            }

            is TdApi.TextEntityTypeTextUrl -> {
                val url = (it.type as TdApi.TextEntityTypeTextUrl).url
                result = result.replace(subText, "[$subText]($url)")
            }

            is TdApi.TextEntityTypeEmailAddress -> {
                result = result.replace(subText, "[$subText]($subText)")
            }

            else -> {
                result = result.replace(subText, subText)
            }
        }
    }

    return result
}