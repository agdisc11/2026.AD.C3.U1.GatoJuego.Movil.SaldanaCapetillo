package com.upchiapas.gato.game.presentation.components.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upchiapas.gato.R
import com.upchiapas.gato.game.presentation.components.atoms.ScoreValue
import com.upchiapas.gato.ui.theme.GatoTheme

/** Molécula: tarjeta con una etiqueta y su contador animado. */
@Composable
fun ScoreCard(
    label: String,
    value: Int,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            ScoreValue(value = value, color = accentColor)
        }
    }
}

@Preview
@Composable
private fun ScoreCardPreview() {
    GatoTheme {
        ScoreCard(
            label = stringResource(R.string.score_draws),
            value = 2,
            accentColor = MaterialTheme.colorScheme.secondary
        )
    }
}
