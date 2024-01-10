package com.zlsp.calcxe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zlsp.calcxe.base.ext.borderGradient
import com.zlsp.calcxe.base.ext.clickableNoRipple
import com.zlsp.calcxe.base.ext.shadow
import com.zlsp.calcxe.domain.Screen
import com.zlsp.calcxe.ui.screens.home.HomeScreen
import com.zlsp.calcxe.ui.screens.list.ListScreen
import com.zlsp.calcxe.ui.screens.settings.SettingsScreen
import com.zlsp.calcxe.ui.theme.AppTheme
import com.zlsp.calcxe.ui.theme.models.ThemeMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val activeScreenRote = navBackStackEntry?.destination?.route
            val activeScreen = getActiveScreen(activeScreenRote)
            val searchHomeValue = remember { mutableStateOf("") }
            val searchListValue = remember { mutableStateOf("") }
            AppTheme(
                themeMode = ThemeMode.AMOLED,
            ) {
                MainWrapper(
                    bottomBarContent = {
                        AppBottomBar(
                            navBackStackEntry = navBackStackEntry,
                            navigate = {
                                navController.navigate(it) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    },
                    screenContent = {
                        AppTopBar(
                            screen = activeScreen,
                            searchValue = when (activeScreen) {
                                Screen.SETTINGS -> ""
                                Screen.HOME -> searchHomeValue.value
                                Screen.LIST -> searchListValue.value
                            },
                            onValueChange = {
                                when(activeScreen) {
                                    Screen.SETTINGS -> Unit
                                    Screen.HOME -> searchHomeValue.value = it
                                    Screen.LIST -> searchListValue.value = it
                                }
                            }
                        )
                        NavHost(
                            modifier = Modifier.padding(bottom = it.calculateBottomPadding()),
                            navController = navController,
                            startDestination = Screen.HOME.route,
                            enterTransition = { fadeIn() },
                            popEnterTransition = { fadeIn() },
                            exitTransition = { fadeOut() },
                            popExitTransition = { fadeOut() }
                        ) {
                            composable(Screen.HOME.route) {
                                HomeScreen()
                            }
                            composable(Screen.SETTINGS.route) {
                                SettingsScreen()
                            }
                            composable(Screen.LIST.route) {
                                ListScreen()
                            }
                        }
                    }
                )
            }
        }
    }

    private fun getActiveScreen(route: String?): Screen {
        return when (route) {
            Screen.LIST.route -> Screen.LIST
            Screen.SETTINGS.route -> Screen.SETTINGS
            else -> Screen.HOME
        }
    }

}

@Composable
private fun MainWrapper(
    bottomBarContent: @Composable () -> Unit,
    screenContent: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        contentColor = MaterialTheme.colorScheme.primary,
        bottomBar = bottomBarContent,
        content = screenContent,
    )
}

@Composable
fun AppTopBar(
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextFieldSearch(
                    value = searchValue,
                    onValueChange = onValueChange
                )
                AnimatedVisibility(screen == Screen.LIST) {
                    Row {
                        Icon(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .size(22.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                            contentDescription = null
                        )
                        Icon(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .size(22.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun RowScope.TextFieldSearch(
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
    BasicTextField(
        modifier = Modifier
            .weight(1f)
            .onFocusChanged {
                isFocused.value = it.isFocused
            },
        value = value,
        onValueChange = onValueChange,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
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
                            color = MaterialTheme.colorScheme.primary.copy(0.6f)
                        )
                    } else {
                        innerTextField.invoke()
                    }
                }
                Icon(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(animationIconAlpha.value)
                )
            }
        }
    }
}

@Composable
fun AppBottomBar(
    navBackStackEntry: NavBackStackEntry?,
    navigate: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
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
            .navigationBarsPadding()
            .padding(10.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(24.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Screen.entries.toTypedArray().forEach { screen ->
                val isActiveScreen = navBackStackEntry.isActiveScreen(screen)
                BottomBarItem(screen, isActiveScreen, navigate)
            }
        }
    }
}

@Composable
private fun RowScope.BottomBarItem(
    screen: Screen,
    isActiveScreen: Boolean,
    onClick: (String) -> Unit
) {
    val animationColorBlur = animateColorAsState(
        targetValue = if (isActiveScreen) {
            MaterialTheme.colorScheme.primary
        } else {
            Color.Transparent
        },
        label = "",
//                    animationSpec = spring(stiffness = 500f)
    )
    val animationColorBackground = animateFloatAsState(
        targetValue = if (isActiveScreen) 1f else 0f,
        label = "",
//                    animationSpec = spring(stiffness = 100f)
    )
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(10.dp)
            .shadow(
                color = animationColorBlur.value,
                blurRadius = 24.dp
            )
            .background(
                MaterialTheme.colorScheme.background.copy(animationColorBackground.value),
                RoundedCornerShape(24.dp)
            )
            .padding(5.dp)
            .clickableNoRipple { onClick(screen.route) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(screen.iconId),
            contentDescription = null,
        )
        Text(
            text = stringResource(screen.labelId),
            fontSize = 10.sp,
        )

    }
}

fun NavBackStackEntry?.isActiveScreen(screen: Screen): Boolean {
    val currentDestination = this?.destination
    return currentDestination?.hierarchy?.any { it.route == screen.route } == true
}

//@Composable
//fun AppBottomBar(navController: NavHostController) {
//    BottomAppBar(
//        modifier = Modifier
//            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
//        contentColor = MaterialTheme.colorScheme.primary,
//        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
//        tonalElevation = 20.dp
//    ) {
//        val navBackStackEntry by navController.currentBackStackEntryAsState()
//        val currentDestination = navBackStackEntry?.destination
//        val screens = listOf(
//            Screen.SETTINGS,
//            Screen.HOME,
//            Screen.LIST
//        )
//        screens.forEach { screen ->
//            val isActiveScreen =
//                currentDestination?.hierarchy?.any { it.route == screen.route } == true
//            NavigationBarItem(
//                selected = isActiveScreen,
//                icon = { Icon(painterResource(screen.iconId), null) },
//                label = { Text(stringResource(screen.labelId)) },
//                alwaysShowLabel = false,
//                colors = NavigationBarItemDefaults.colors(
//                    selectedIconColor = MaterialTheme.colorScheme.primary,
//                    selectedTextColor = MaterialTheme.colorScheme.primary,
//                    indicatorColor = MaterialTheme.colorScheme.background,
//                    unselectedIconColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
//                ),
//                onClick = {
//                    navController.navigate(screen.route) {
//                        popUpTo(navController.graph.findStartDestination().id) {
//                            saveState = true
//                        }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                }
//            )
//        }
//    }
//}