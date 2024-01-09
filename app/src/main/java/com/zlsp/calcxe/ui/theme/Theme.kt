package com.zlsp.calcxe.ui.theme

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zlsp.calcxe.ui.theme.models.AppColorScheme
import com.zlsp.calcxe.ui.theme.models.ThemeMode

@Composable
fun AppTheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    appColorScheme: AppColorScheme = AppColorScheme.GREEN,
    darkTheme: Boolean = isSystemInDarkTheme(),
    systemUiController: SystemUiController = rememberSystemUiController(),
    isDynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = when {
        isDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme)
                dynamicDarkColorScheme(context).switch()
            else
                dynamicLightColorScheme(context).switch()
        }

        else -> appColorScheme.paletteSet.getPalette(themeMode)
    }
    val isDarkIcons = if (isDynamicColor) !darkTheme else themeMode == ThemeMode.LIGHT
    val systemBarsColor = colors.switch().background
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = systemBarsColor,
            darkIcons = isDarkIcons
        )
    }

    MaterialTheme(
        colorScheme = colors.switch(),
        typography = Typography,
        content = content
    )
}

@Composable
private fun ColorScheme.switch() = this.copy(
    primary = animateColor(targetColor = this.primary),
    onPrimary = animateColor(targetColor = this.onPrimary),
    background = animateColor(targetColor = this.background),
    surface = animateColor(targetColor = this.surface)
)

@Composable
private fun animateColor(targetColor: Color) =
    animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 1000),
        label = ""
    ).value