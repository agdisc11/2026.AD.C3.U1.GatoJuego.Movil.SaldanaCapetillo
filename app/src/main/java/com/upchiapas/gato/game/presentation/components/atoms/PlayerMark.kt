package com.upchiapas.gato.game.presentation.components.atoms

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.upchiapas.gato.game.model.Player
import com.upchiapas.gato.ui.theme.GatoTheme
import com.upchiapas.gato.ui.theme.markColor

/** Átomo: el símbolo X u O con el color de su jugador. */
@Composable
fun PlayerMark(player: Player, modifier: Modifier = Modifier) {
    Text(
        text = player.name,
        modifier = modifier,
        color = player.markColor(),
        style = MaterialTheme.typography.displayMedium
    )
}

@Preview(showBackground = true)
@Composable
private fun PlayerMarkPreview() {
    GatoTheme {
        PlayerMark(player = Player.O)
    }
}
