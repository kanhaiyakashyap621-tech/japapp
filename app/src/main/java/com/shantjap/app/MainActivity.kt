package com.shantjap.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.shantjap.app.presentation.navigation.ShantNavGraph
import com.shantjap.app.presentation.theme.ShantJapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShantJapTheme {
                ShantNavGraph()
            }
        }
    }
}
