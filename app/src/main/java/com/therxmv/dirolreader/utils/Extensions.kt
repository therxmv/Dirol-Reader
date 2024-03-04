package com.therxmv.dirolreader.utils

import org.drinkless.td.libcore.telegram.TdApi

fun TdApi.FormattedText.toMarkdown(): String {
    var result = this.text

    this.entities.forEach {
        val startIndex = it.offset
        val endIndex = startIndex + it.length
        var subText = this.text.subSequence(startIndex, endIndex).toString()

        // remove line break/space/etc from the end of the subString
        while (subText.last().isWhitespace()) {
            subText = subText.dropLast(1)
        }

        // remove line break if it is in the middle of the subString
        val strList = if (subText.contains(10.toChar())) {
            subText.split(10.toChar())
        } else listOf(subText)

        strList.forEach { str ->
            when (it.type) {
                is TdApi.TextEntityTypeBold -> {
                    result = result.replace(str, "**$str**")
                }

                is TdApi.TextEntityTypeItalic -> {
                    result = result.replace(str, "_${str}_")
                }

                is TdApi.TextEntityTypeStrikethrough -> {
                    result = result.replace(str, "~~$str~~")
                }

                is TdApi.TextEntityTypeUnderline -> {
                    result = result.replace(str, "__${str}__")
                }

                is TdApi.TextEntityTypeCode -> {
                    result = result.replace(str, "`$str`")
                }

                is TdApi.TextEntityTypePre -> {
                    result = result.replace(str, "```$str```")
                }

                is TdApi.TextEntityTypeUrl -> {
                    result = result.replace(str, "[$str]($str)")
                }

                is TdApi.TextEntityTypeTextUrl -> {
                    val url = (it.type as TdApi.TextEntityTypeTextUrl).url
                    result = result.replace(str, "[$str]($url)")
                }

                is TdApi.TextEntityTypeEmailAddress -> {
                    result = result.replace(str, "[$str]($str)")
                }

                else -> {
                    result = result.replace(str, str)
                }
            }
        }
    }

    return result.map {
        if (it.code == 10) "\n\r" else it
    }.joinToString("")
}