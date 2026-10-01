package com.upchiapas.gato

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.upchiapas.gato.game.presentation.GameRoute
import com.upchiapas.gato.ui.theme.GatoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GatoTheme {
                GameRoute()
            }
        }
    }
}
