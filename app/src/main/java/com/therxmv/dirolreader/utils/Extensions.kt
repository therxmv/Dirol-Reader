package com.therxmv.dirolreader.utils

import androidx.compose.ui.Modifier

inline fun Modifier.thenIf(
    predicate: Boolean,
    modifier: Modifier.() -> Modifier,
): Modifier =
    if (predicate) {
        this.modifier()
    } else {
        this
    }