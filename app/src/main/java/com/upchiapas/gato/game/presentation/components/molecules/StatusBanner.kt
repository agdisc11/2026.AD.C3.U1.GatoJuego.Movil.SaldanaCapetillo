package com.upchiapas.gato.game.presentation.components.molecules

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upchiapas.gato.R
import com.upchiapas.gato.game.model.GameStatus
import com.upchiapas.gato.game.model.Player
import com.upchiapas.gato.ui.theme.GatoTheme
import com.upchiapas.gato.ui.theme.containerColor

/** Molécula: mensaje de turno, ganador o empate con su color. */
@Composable
fun StatusBanner(status: GameStatus, modifier: Modifier = Modifier) {
    val message = when (status) {
        is GameStatus.Playing -> stringResource(R.string.status_turn, status.turn.name)
        is GameStatus.Won -> stringResource(R.string.status_winner, status.winner.name)
        GameStatus.Draw -> stringResource(R.string.status_draw)
    }
    val targetColor = when (status) {
        is GameStatus.Playing -> MaterialTheme.colorScheme.secondaryContainer
        is GameStatus.Won -> status.winner.containerColor()
        GameStatus.Draw -> MaterialTheme.colorScheme.surfaceVariant
    }
    val containerColor by animateColorAsState(targetValue = targetColor, label = "statusColor")

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        color = containerColor,
        contentColor = MaterialTheme.colorScheme.contentColorFor(targetColor)
    ) {
        AnimatedContent(targetState = message, label = "statusMessage") { text ->
            Text(
                text = text,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun StatusBannerPreview() {
    GatoTheme {
        StatusBanner(status = GameStatus.Won(Player.O))
    }
}
