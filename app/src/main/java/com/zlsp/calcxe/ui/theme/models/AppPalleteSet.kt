package com.zlsp.calcxe.ui.theme.models

import androidx.compose.material3.ColorScheme

data class AppPaletteSet(
    val light: ColorScheme,
    val dark: ColorScheme,
    val amoled: ColorScheme
) {
    fun getPalette(themeMode: ThemeMode): ColorScheme =
        when(themeMode) {
            ThemeMode.LIGHT -> light
            ThemeMode.DARK -> dark
            ThemeMode.AMOLED -> amoled
        }
}
