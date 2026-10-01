package com.upchiapas.gato.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.upchiapas.gato.game.model.Player

/** Color del símbolo de cada jugador, tomado del tema. */
@Composable
fun Player.markColor(): Color = when (this) {
    Player.X -> MaterialTheme.colorScheme.primary
    Player.O -> MaterialTheme.colorScheme.tertiary
}

/** Color de fondo suave para resaltar al jugador (casillas ganadoras, banner). */
@Composable
fun Player.containerColor(): Color = when (this) {
    Player.X -> MaterialTheme.colorScheme.primaryContainer
    Player.O -> MaterialTheme.colorScheme.tertiaryContainer
}
