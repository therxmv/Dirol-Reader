package com.therxmv.dirolreader.ui.commonview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

enum class FillOptions {
    WIDTH,
    HEIGHT,
    BOTH,
}

@Composable
fun CenteredBoxLoader(
    modifier: Modifier = Modifier,
    option: FillOptions = FillOptions.BOTH,
) {
    Box(
        modifier = modifier.fillWithOption(option),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Modifier.fillWithOption(option: FillOptions) = when (option) {
    FillOptions.WIDTH -> fillMaxWidth()
    FillOptions.HEIGHT -> fillMaxHeight()
    FillOptions.BOTH -> fillMaxSize()
}