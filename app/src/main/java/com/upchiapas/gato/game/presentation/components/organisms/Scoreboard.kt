package com.upchiapas.gato.game.presentation.components.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upchiapas.gato.R
import com.upchiapas.gato.game.model.Player
import com.upchiapas.gato.game.presentation.ScoreUiState
import com.upchiapas.gato.game.presentation.components.molecules.ScoreCard
import com.upchiapas.gato.ui.theme.GatoTheme
import com.upchiapas.gato.ui.theme.markColor

/** Organismo: marcador con victorias de X, empates y victorias de O. */
@Composable
fun Scoreboard(score: ScoreUiState, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ScoreCard(
            label = stringResource(R.string.score_player, Player.X.name),
            value = score.winsX,
            accentColor = Player.X.markColor(),
            modifier = Modifier.weight(1f)
        )
        ScoreCard(
            label = stringResource(R.string.score_draws),
            value = score.draws,
            accentColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.weight(1f)
        )
        ScoreCard(
            label = stringResource(R.string.score_player, Player.O.name),
            value = score.winsO,
            accentColor = Player.O.markColor(),
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScoreboardPreview() {
    GatoTheme {
        Scoreboard(score = ScoreUiState(winsX = 2, winsO = 1, draws = 3))
    }
}
