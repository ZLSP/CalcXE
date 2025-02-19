package com.zlsp.calcxe.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zlsp.calcxe.R
import com.zlsp.calcxe.base.ext.borderGradient
import com.zlsp.calcxe.domain.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@NonRestartableComposable
@Composable
fun BottomBar(
    activeScreen: State<Screen>,
    selectedCategory: Boolean,
    selectedFavorite: Boolean,
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    onChangeFilterFavorite: () -> Unit,
    onChangeVisibleCategory: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp
                )
            )
            .borderGradient(
                list = listOf(
                    MaterialTheme.colorScheme.primary,
                    Color.Transparent,
                    Color.Transparent,
                ),
                shape = RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp
                )
            )
            .navigationBarsPadding(),
    ) {
        AnimatedVisibility(
            visible = activeScreen.value == Screen.LIST,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilterItem(
                    isSelected = selectedCategory,
                    iconRes = R.drawable.ic_bottom_nav_list,
                    textRes = R.string.filterCategories,
                    onClick = onChangeVisibleCategory
                )
                IconButton(
                    onClick = {},
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = null
                    )
                }
                FilterItem(
                    isSelected = selectedFavorite,
                    iconRes = R.drawable.ic_favorite,
                    textRes = R.string.filterFavorite,
                    onClick = onChangeFilterFavorite
                )
            }
        }
        TabRow(
            containerColor = MaterialTheme.colorScheme.background,
            selectedTabIndex = pagerState.currentPage
        ) {
            Screen.entries.forEachIndexed { index, tab ->
                Tab(
                    modifier = Modifier.clip(
                        RoundedCornerShape(
                            topStart = 24.dp,
                            topEnd = 24.dp,
                        )
                    ),
                    selected = pagerState.currentPage == index,
                    unselectedContentColor = MaterialTheme.colorScheme.primary.copy(
                        0.5f
                    ),
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    text = {
                        Text(
                            text = stringResource(tab.labelId),
                            fontSize = 10.sp,
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(tab.iconId),
                            contentDescription = null,
                        )
                    },
                    onClick = {
                        if (pagerState.currentPage != index) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    }
                )
            }
        }
    }
}

@NonRestartableComposable
@Composable
private fun RowScope.FilterItem(
    isSelected: Boolean,
    iconRes: Int,
    textRes: Int,
    onClick: () -> Unit
) {
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            Color.Transparent
        }
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.background
        } else {
            MaterialTheme.colorScheme.primary
        }
    )
    Button(
        modifier = Modifier
            .padding(5.dp)
            .weight(1f),
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        contentPadding = PaddingValues(3.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = null
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = stringResource(textRes),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}