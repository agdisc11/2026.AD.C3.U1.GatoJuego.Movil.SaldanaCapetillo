package com.upchiapas.gato.game.presentation

/**
 * Acciones que la UI puede notificar al ViewModel (flujo hacia arriba en UDF).
 * La UI nunca modifica el estado: solo envía uno de estos eventos.
 */
sealed interface GameEvent {

    /** El usuario tocó la casilla [index] (0 a 8, de izquierda a derecha y de arriba abajo). */
    data class CellClicked(val index: Int) : GameEvent

    /** Limpia el tablero conservando el marcador. */
    data object NewRound : GameEvent

    /** Vuelve todo al estado inicial, incluido el marcador. */
    data object ResetScore : GameEvent
}
