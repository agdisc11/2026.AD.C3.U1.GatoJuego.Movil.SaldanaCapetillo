package com.upchiapas.gato.game.model

/**
 * Estados posibles de una ronda. Al ser una interfaz sellada, el compilador
 * obliga a la UI a contemplar los tres casos en cada `when`.
 */
sealed interface GameStatus {

    /** La ronda sigue en curso y le toca a [turn]. */
    data class Playing(val turn: Player) : GameStatus

    /** [winner] completó una línea de tres. */
    data class Won(val winner: Player) : GameStatus

    /** Se llenó el tablero sin ganador. */
    data object Draw : GameStatus
}
