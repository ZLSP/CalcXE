package com.zlsp.calcxe.ui.view.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@NonRestartableComposable
@Composable
fun BoxScope.GradientLine(
    alignment: Alignment,
    colorTop: Color,
    colorBottom: Color
) {
    Box(
        Modifier.Companion
            .align(alignment)
            .fillMaxWidth()
            .height(10.dp)
            .background(
                Brush.verticalGradient(
                    listOf(
                        colorTop,
                        colorBottom
                    )
                )
            )
    )
}