package com.upchiapas.gato.game.presentation.components.molecules

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upchiapas.gato.game.model.Player
import com.upchiapas.gato.game.presentation.CellUiState
import com.upchiapas.gato.game.presentation.components.atoms.PlayerMark
import com.upchiapas.gato.ui.theme.GatoTheme
import com.upchiapas.gato.ui.theme.containerColor

/**
 * Molécula: una casilla del tablero. Es stateless: recibe su estado ya
 * calculado y avisa del toque con [onClick]; no sabe nada del ViewModel.
 */
@Composable
fun BoardCell(
    cell: CellUiState,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Las casillas de la línea ganadora toman el color del ganador.
    val backgroundColor by animateColorAsState(
        targetValue = if (cell.isWinning && cell.player != null) {
            cell.player.containerColor()
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        label = "cellBackground"
    )

    Surface(
        onClick = onClick,
        enabled = cell.isClickable,
        modifier = modifier
            .aspectRatio(1f)
            .semantics { contentDescription = description },
        shape = MaterialTheme.shapes.large,
        color = backgroundColor
    ) {
        Box(contentAlignment = Alignment.Center) {
            AnimatedVisibility(
                visible = cell.player != null,
                enter = scaleIn() + fadeIn()
            ) {
                cell.player?.let { player -> PlayerMark(player = player) }
            }
        }
    }
}

@Preview
@Composable
private fun BoardCellPreview() {
    GatoTheme {
        BoardCell(
            cell = CellUiState(player = Player.X, isWinning = true, isClickable = false),
            description = "",
            onClick = {},
            modifier = Modifier.size(96.dp)
        )
    }
}
