package com.zlsp.calcxe.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zlsp.calcxe.R
import com.zlsp.calcxe.base.ext.borderGradient
import com.zlsp.calcxe.base.ext.clickableNoRipple
import com.zlsp.calcxe.base.ext.shadow
import com.zlsp.calcxe.domain.Screen

@NonRestartableComposable
@Composable
fun TopBar(
    screen: Screen,
    searchValue: String,
    onValueChange: (String) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .borderGradient(
                list = listOf(
                    Color.Transparent,
                    Color.Transparent,
                    MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                )
            )
            .statusBarsPadding()
            .padding(10.dp),
    ) {
        AnimatedVisibility(screen != Screen.SETTINGS) {
            Column {
                TextFieldSearch(
                    value = searchValue,
                    onValueChange = onValueChange
                )
            }
        }
    }
}

@NonRestartableComposable
@Composable
private fun TextFieldSearch(
    value: String,
    onValueChange: (String) -> Unit,
) {
    val isFocused = remember { mutableStateOf(false) }
    val animationColorBlur = animateColorAsState(
        targetValue = if (isFocused.value) {
            MaterialTheme.colorScheme.primary
        } else {
            Color.Transparent
        },
        label = "",
    )
    val focusManager = LocalFocusManager.current
    val animationIconAlpha = animateFloatAsState(
        targetValue = if (isFocused.value) 1f else 0.6f,
        label = ""
    )
    val isVisibleKeyboard = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    if (isVisibleKeyboard.not()) {
        LaunchedEffect(Unit) {
            println("clearFocus")
            focusManager.clearFocus()
        }
    }

    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                isFocused.value = it.isFocused
            },
        value = value,
        onValueChange = onValueChange,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        textStyle = TextStyle.Default.copy(
            color = MaterialTheme.colorScheme.primary,
        ),
        singleLine = true,
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() },
        )
    ) { innerTextField ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    color = animationColorBlur.value,
                    blurRadius = 24.dp
                ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(
                width = 1.dp,
                MaterialTheme.colorScheme.primary
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isBlank()) {
                        Text(
                            text = "Введите название продукта",
                            color = MaterialTheme.colorScheme.primary.copy(0.6f),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    } else {
                        innerTextField.invoke()
                    }
                }
                Crossfade(isFocused.value) { condition ->
                    val iconRes = if (condition) {
                        R.drawable.ic_clear_text
                    } else {
                        R.drawable.ic_search
                    }
                    Icon(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(20.dp)
                            .clickableNoRipple {
                                if (condition) {
                                    onValueChange("")
                                }
                            },
                        imageVector = ImageVector.vectorResource(iconRes),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(animationIconAlpha.value)
                    )
                }

            }
        }
    }
}