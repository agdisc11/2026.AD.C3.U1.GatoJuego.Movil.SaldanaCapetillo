package com.upchiapas.gato.game.presentation

import com.upchiapas.gato.game.model.GameStatus
import com.upchiapas.gato.game.model.Player
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GameViewModelTest {

    private lateinit var viewModel: GameViewModel

    private val state get() = viewModel.uiState.value

    @Before
    fun setUp() {
        viewModel = GameViewModel()
    }

    private fun play(vararg indexes: Int) {
        indexes.forEach { viewModel.onEvent(GameEvent.CellClicked(it)) }
    }

    @Test
    fun initialState_isTurnOfX_withEmptyBoard() {
        assertEquals(GameStatus.Playing(Player.X), state.status)
        assertTrue(state.cells.all { it.player == null && it.isClickable })
    }

    @Test
    fun cellClicked_placesMark_andPassesTurn() {
        play(4)

        assertEquals(Player.X, state.cells[4].player)
        assertFalse(state.cells[4].isClickable)
        assertEquals(GameStatus.Playing(Player.O), state.status)
    }

    @Test
    fun clickingOccupiedCell_isIgnored() {
        play(4, 4)

        assertEquals(Player.X, state.cells[4].player)
        assertEquals(GameStatus.Playing(Player.O), state.status)
    }

    @Test
    fun threeInARow_winsMarksLine_andUpdatesScore() {
        play(0, 3, 1, 4, 2) // X: 0,1,2  O: 3,4

        assertEquals(GameStatus.Won(Player.X), state.status)
        assertEquals(listOf(0, 1, 2), state.cells.indices.filter { state.cells[it].isWinning })
        assertEquals(1, state.score.winsX)
        assertTrue(state.cells.none { it.isClickable })
    }

    @Test
    fun clicksAfterWin_areIgnored() {
        play(0, 3, 1, 4, 2, 5)

        assertNull(state.cells[5].player)
        assertEquals(1, state.score.winsX)
    }

    @Test
    fun fullBoardWithoutLine_isDraw() {
        play(0, 1, 2, 4, 3, 5, 7, 6, 8)

        assertEquals(GameStatus.Draw, state.status)
        assertEquals(1, state.score.draws)
    }

    @Test
    fun newRound_clearsBoard_keepsScore_andAlternatesStarter() {
        play(0, 3, 1, 4, 2)
        viewModel.onEvent(GameEvent.NewRound)

        assertTrue(state.cells.all { it.player == null && it.isClickable })
        assertEquals(1, state.score.winsX)
        assertEquals(GameStatus.Playing(Player.O), state.status)
    }

    @Test
    fun resetScore_restoresInitialState() {
        play(0, 3, 1, 4, 2)
        viewModel.onEvent(GameEvent.NewRound)
        viewModel.onEvent(GameEvent.ResetScore)

        assertEquals(GameUiState(), state)
    }
}
