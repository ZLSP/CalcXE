package com.zlsp.calcxe.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zlsp.calcxe.ui.theme.AppTheme
import com.zlsp.calcxe.ui.theme.models.ThemeMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeMode = remember {
                mutableStateOf(ThemeMode.DARK)
            }
            val isDynamicColor = remember {
                mutableStateOf(true)
            }
            AppTheme(
                themeMode = themeMode.value,
                isDynamicColor = isDynamicColor.value
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                    Column {
                        Checkbox(
                            checked = isDynamicColor.value,
                            onCheckedChange = {
                                isDynamicColor.value = !isDynamicColor.value
                            }
                        )
                        Button(onClick = {
                            if (themeMode.value == ThemeMode.DARK) {
                                themeMode.value = ThemeMode.LIGHT
                            } else {
                                themeMode.value = ThemeMode.DARK
                            }
                        }) {

                        }
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        Greeting("Android")
    }
}