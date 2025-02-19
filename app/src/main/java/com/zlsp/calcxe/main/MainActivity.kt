package com.zlsp.calcxe.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.zlsp.calcxe.domain.Screen
import com.zlsp.calcxe.main.MainContract.*
import com.zlsp.calcxe.ui.screens.home.HomeScreen
import com.zlsp.calcxe.ui.screens.products.ProductsScreen
import com.zlsp.calcxe.ui.screens.settings.SettingsScreen
import com.zlsp.calcxe.ui.theme.AppTheme
import com.zlsp.calcxe.ui.theme.models.ThemeMode
import com.zlsp.calcxe.ui.view.BottomBar
import com.zlsp.calcxe.ui.view.TopBar
import com.zlsp.calcxe.ui.view.main.GradientLine
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val viewModel = koinViewModel<MainViewModel>()
            val state = viewModel.collectAsState().value
            LaunchedEffect(Unit) {
                println(state)
            }
            val coroutineScope = rememberCoroutineScope()
            val pagerState = rememberPagerState(
                initialPage = Screen.HOME.ordinal
            ) {
                Screen.entries.size
            }
            val activeScreen = remember {
                derivedStateOf(structuralEqualityPolicy()) {
                    Screen.entries[pagerState.currentPage]
                }
            }
            val searchHomeValue = remember { mutableStateOf("") }
            val visibleCategory = remember { mutableStateOf(true) }
            AppTheme(
                themeMode = ThemeMode.DARK,
            ) {
                MainWrapper(
                    bottomBarContent = {
                        BottomBar(
                            activeScreen = activeScreen,
                            selectedCategory = visibleCategory.value,
                            selectedFavorite = state.productsScreenState.isOnlyFavorite.value,
                            pagerState = pagerState,
                            coroutineScope = coroutineScope,
                            onChangeFilterFavorite = {
                                viewModel.sendAction(Action.ProductsScreenActions.ChangeStateFilterFavorite)
                            },
                            onChangeVisibleCategory = {
                                visibleCategory.value = !visibleCategory.value
                            }
                        )

                    },
                    topBarContent = {
                        TopBar(
                            screen = activeScreen.value,
                            searchValue = when (activeScreen.value) {
                                Screen.SETTINGS -> ""
                                Screen.HOME -> state.searchHomeProducts.value
                                Screen.LIST -> state.productsScreenState.searchList.value
                            },
                            onValueChange = {
                                when (activeScreen.value) {
                                    Screen.SETTINGS -> Unit
                                    Screen.HOME -> searchHomeValue.value = it
                                    Screen.LIST -> state.productsScreenState.searchList.value = it
                                }
                            }
                        )
                    },
                    screenContent = {
                        HorizontalPager(
                            state = pagerState,
                            beyondViewportPageCount = 1,
                            verticalAlignment = Alignment.Top
                        ) {
                            when (it) {
                                Screen.SETTINGS.ordinal -> {
                                    SettingsScreen()
                                }

                                Screen.HOME.ordinal -> {
                                    HomeScreen()
                                }

                                Screen.LIST.ordinal -> {
                                    ProductsScreen(
                                        categories = state.productsScreenList,
                                        selectedFilterCategories = visibleCategory.value,
                                        selectedFilterFavorite = state.productsScreenState.isOnlyFavorite.value,
                                        sendAction = viewModel::sendAction
                                    )
                                }
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
private fun MainWrapper(
    bottomBarContent: @Composable () -> Unit,
    topBarContent: @Composable () -> Unit,
    screenContent: @Composable () -> Unit
) {
    Scaffold(
        contentColor = MaterialTheme.colorScheme.primary,
        bottomBar = bottomBarContent,
        content = {
            Box(Modifier.padding(bottom = it.calculateBottomPadding())) {
                Column {
                    topBarContent()
                    Box {
                        screenContent()
                        GradientLine(
                            alignment = Alignment.TopCenter,
                            colorTop = MaterialTheme.colorScheme.background,
                            colorBottom = Color.Transparent
                        )
                    }
                }
                GradientLine(
                    alignment = Alignment.BottomCenter,
                    colorTop = Color.Transparent,
                    colorBottom = MaterialTheme.colorScheme.background
                )
            }

        },
    )
}