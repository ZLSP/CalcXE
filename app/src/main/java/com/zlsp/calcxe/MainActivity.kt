package com.zlsp.calcxe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zlsp.calcxe.domain.Screen
import com.zlsp.calcxe.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppTheme {
                MainWrapper(
                    bottomBarContent = { AppBottomBar(navController) },
                    screenContent = {
                        NavHost(
                            modifier = Modifier.padding(it),
                            navController = navController,
                            startDestination = Screen.HOME.route
                        ) {
                            composable(Screen.HOME.route) {

                            }
                            composable(Screen.SETTINGS.route) {

                            }
                            composable(Screen.LIST.route) {

                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun MainWrapper(
    bottomBarContent: @Composable () -> Unit,
    screenContent: @Composable (PaddingValues) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            contentColor = MaterialTheme.colorScheme.primary,
            bottomBar = bottomBarContent,
            content = screenContent
        )
    }
}

@Composable
fun AppBottomBar(navController: NavHostController) {
    BottomAppBar(
        modifier = Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        tonalElevation = 20.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val screens = listOf(
            Screen.SETTINGS,
            Screen.HOME,
            Screen.LIST
        )
        screens.forEach { screen ->
            val isActiveScreen =
                currentDestination?.hierarchy?.any { it.route == screen.route } == true
            NavigationBarItem(
                selected = isActiveScreen,
                icon = { Icon(painterResource(screen.iconId), null) },
                label = { Text(stringResource(screen.labelId)) },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.background,
                    unselectedIconColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                ),
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}