package com.upchiapas.gato.game.presentation

import com.upchiapas.gato.game.model.GameStatus
import com.upchiapas.gato.game.model.Player

/** Número de casillas del tablero (3 x 3). */
const val BOARD_SIZE = 9

/**
 * Estado de una casilla, ya resuelto por el ViewModel:
 * la UI solo lo dibuja, no decide nada.
 */
data class CellUiState(
    val player: Player? = null,
    val isWinning: Boolean = false,
    val isClickable: Boolean = true
)

/** Victorias acumuladas entre rondas. */
data class ScoreUiState(
    val winsX: Int = 0,
    val winsO: Int = 0,
    val draws: Int = 0
)

/**
 * Estado completo de la pantalla. Todas sus propiedades son `val`:
 * para cambiar algo, el ViewModel crea una copia nueva con `copy()`.
 */
data class GameUiState(
    val cells: List<CellUiState> = List(BOARD_SIZE) { CellUiState() },
    val status: GameStatus = GameStatus.Playing(Player.X),
    val score: ScoreUiState = ScoreUiState()
)
