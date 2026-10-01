package com.upchiapas.gato.game.presentation

import androidx.lifecycle.ViewModel
import com.upchiapas.gato.game.model.GameStatus
import com.upchiapas.gato.game.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    // Backing property: solo el ViewModel puede escribir en el estado...
    private val _uiState = MutableStateFlow(GameUiState())

    // ...y la UI recibe una versión de solo lectura.
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    // Quién empieza la ronda actual; se alterna en cada ronda nueva.
    private var roundStarter = Player.X

    /** Único punto de entrada de eventos (UDF): la UI llama aquí y nada más. */
    fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.CellClicked -> playTurn(event.index)
            GameEvent.NewRound -> startNewRound()
            GameEvent.ResetScore -> resetScore()
        }
    }

    private fun playTurn(index: Int) {
        _uiState.update { current ->
            val status = current.status
            // Regla de negocio: solo se tira si la ronda sigue y la casilla está libre.
            if (status !is GameStatus.Playing || current.cells[index].player != null) {
                current
            } else {
                applyMove(current, index, status.turn)
            }
        }
    }

    private fun applyMove(current: GameUiState, index: Int, player: Player): GameUiState {
        // Nuevo tablero (no se modifica el anterior): solo cambia la casilla tocada.
        val board = current.cells.mapIndexed { i, cell -> if (i == index) player else cell.player }
        val winningLine = findWinningLine(board)

        val newStatus = when {
            winningLine != null -> GameStatus.Won(player)
            board.all { it != null } -> GameStatus.Draw
            else -> GameStatus.Playing(player.next())
        }

        return current.copy(
            cells = buildCells(board, winningLine.orEmpty(), newStatus is GameStatus.Playing),
            status = newStatus,
            score = updateScore(current.score, newStatus)
        )
    }

    private fun startNewRound() {
        roundStarter = roundStarter.next()
        _uiState.update { current ->
            current.copy(
                cells = GameUiState().cells,
                status = GameStatus.Playing(roundStarter)
            )
        }
    }

    private fun resetScore() {
        roundStarter = Player.X
        _uiState.value = GameUiState()
    }

    /** Busca una línea de tres casillas iguales; regresa sus índices o `null`. */
    private fun findWinningLine(board: List<Player?>): List<Int>? =
        WINNING_LINES.firstOrNull { (a, b, c) ->
            board[a] != null && board[a] == board[b] && board[a] == board[c]
        }

    /** Traduce el tablero a estados de casilla listos para dibujar. */
    private fun buildCells(
        board: List<Player?>,
        winningLine: List<Int>,
        isPlaying: Boolean
    ): List<CellUiState> =
        board.mapIndexed { index, player ->
            CellUiState(
                player = player,
                isWinning = index in winningLine,
                isClickable = isPlaying && player == null
            )
        }

    private fun updateScore(score: ScoreUiState, status: GameStatus): ScoreUiState =
        when (status) {
            is GameStatus.Won ->
                if (status.winner == Player.X) score.copy(winsX = score.winsX + 1)
                else score.copy(winsO = score.winsO + 1)
            GameStatus.Draw -> score.copy(draws = score.draws + 1)
            is GameStatus.Playing -> score
        }

    private companion object {
        // Las 8 combinaciones ganadoras, por índice de casilla.
        val WINNING_LINES = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // filas
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // columnas
            listOf(0, 4, 8), listOf(2, 4, 6)                   // diagonales
        )
    }
}
