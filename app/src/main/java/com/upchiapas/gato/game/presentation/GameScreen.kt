package com.upchiapas.gato.game.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.upchiapas.gato.R
import com.upchiapas.gato.game.model.GameStatus
import com.upchiapas.gato.game.model.Player
import com.upchiapas.gato.game.presentation.components.molecules.StatusBanner
import com.upchiapas.gato.game.presentation.components.organisms.GameBoard
import com.upchiapas.gato.game.presentation.components.organisms.Scoreboard
import com.upchiapas.gato.ui.theme.GatoTheme

/**
 * Punto de conexión entre la vista y el ViewModel (el único composable
 * que conoce al ViewModel). Observa el estado y reenvía los eventos.
 */
@Composable
fun GameRoute(viewModel: GameViewModel = viewModel()) {
    // Cada vez que el StateFlow emite un estado nuevo, Compose recompone.
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GameScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

/** Página: dibuja el estado recibido y notifica eventos hacia arriba. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    uiState: GameUiState,
    onEvent: (GameEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = { onEvent(GameEvent.ResetScore) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_refresh),
                            contentDescription = stringResource(R.string.action_reset_score)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Scoreboard(score = uiState.score)

            StatusBanner(status = uiState.status)

            GameBoard(
                cells = uiState.cells,
                onCellClick = { index -> onEvent(GameEvent.CellClicked(index)) },
                modifier = Modifier.widthIn(max = 420.dp)
            )

            Button(onClick = { onEvent(GameEvent.NewRound) }) {
                Text(text = stringResource(R.string.action_new_round))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GameScreenPreview() {
    GatoTheme {
        GameScreen(
            uiState = GameUiState(
                cells = List(BOARD_SIZE) { index ->
                    when (index) {
                        0, 4 -> CellUiState(player = Player.X, isClickable = false)
                        2 -> CellUiState(player = Player.O, isClickable = false)
                        else -> CellUiState()
                    }
                },
                status = GameStatus.Playing(Player.O),
                score = ScoreUiState(winsX = 1, winsO = 2, draws = 1)
            ),
            onEvent = {}
        )
    }
}
