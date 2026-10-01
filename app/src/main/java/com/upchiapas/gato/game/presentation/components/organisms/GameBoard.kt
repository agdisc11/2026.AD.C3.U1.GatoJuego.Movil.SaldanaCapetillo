package com.upchiapas.gato.game.presentation.components.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upchiapas.gato.R
import com.upchiapas.gato.game.model.Player
import com.upchiapas.gato.game.presentation.BOARD_SIZE
import com.upchiapas.gato.game.presentation.CellUiState
import com.upchiapas.gato.game.presentation.components.molecules.BoardCell
import com.upchiapas.gato.ui.theme.GatoTheme

private const val COLUMNS = 3

/**
 * Organismo: el tablero de 3 x 3. Recibe la lista de casillas y avisa
 * qué índice se tocó con [onCellClick].
 */
@Composable
fun GameBoard(
    cells: List<CellUiState>,
    onCellClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        cells.chunked(COLUMNS).forEachIndexed { rowIndex, row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEachIndexed { columnIndex, cell ->
                    val index = rowIndex * COLUMNS + columnIndex
                    val content = cell.player?.name ?: stringResource(R.string.cell_empty)
                    BoardCell(
                        cell = cell,
                        description = stringResource(R.string.cell_description, index + 1, content),
                        onClick = { onCellClick(index) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GameBoardPreview() {
    GatoTheme {
        GameBoard(
            cells = List(BOARD_SIZE) { index ->
                when (index) {
                    0, 4 -> CellUiState(player = Player.X, isClickable = false)
                    2 -> CellUiState(player = Player.O, isClickable = false)
                    else -> CellUiState()
                }
            },
            onCellClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
