package com.upchiapas.gato.game.model

/** Los dos jugadores del gato. El nombre del enum es el símbolo que se dibuja. */
enum class Player {
    X,
    O;

    /** Devuelve el jugador contrario; se usa para alternar turnos. */
    fun next(): Player = if (this == X) O else X
}
