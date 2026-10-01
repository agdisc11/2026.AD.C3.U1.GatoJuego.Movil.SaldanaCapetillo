package com.upchiapas.gato.game.presentation.components.atoms

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.upchiapas.gato.ui.theme.GatoTheme

/** Átomo: número del marcador que se desliza hacia arriba cuando cambia. */
@Composable
fun ScoreValue(value: Int, color: Color, modifier: Modifier = Modifier) {
    AnimatedContent(
        targetState = value,
        modifier = modifier,
        transitionSpec = {
            (slideInVertically { height -> height } + fadeIn()) togetherWith
                (slideOutVertically { height -> -height } + fadeOut())
        },
        label = "scoreValue"
    ) { score ->
        Text(
            text = score.toString(),
            color = color,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScoreValuePreview() {
    GatoTheme {
        ScoreValue(value = 3, color = MaterialTheme.colorScheme.primary)
    }
}
