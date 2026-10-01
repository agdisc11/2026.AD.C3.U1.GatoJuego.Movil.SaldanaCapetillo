# Gato — SH01 Aplicación de utilería

Juego del gato (tic-tac-toe) para dos jugadores en el mismo teléfono, hecho con
Jetpack Compose siguiendo **MVVM + Flujo Unidireccional de Datos (UDF)** con
`StateFlow` y **diseño atómico**.

- Tablero de 3 x 3 con turnos alternados (X / O).
- Detecta ganador (8 líneas posibles) y empate, y resalta la línea ganadora.
- Marcador acumulado de victorias de X, victorias de O y empates.
- "Nueva ronda" limpia el tablero y alterna quién empieza; el botón ↻ reinicia el marcador.
- Sin hardware, sin persistencia ni internet: toda la lógica vive en memoria en el ViewModel.

## Arquitectura

```
                    GameEvent (CellClicked, NewRound, ResetScore)
     ┌──────────────┐  ───────────────────────────────▶  ┌────────────────┐
     │  GameScreen  │          onEvent(event)            │ GameViewModel  │
     │   (Vista)    │                                    │                │
     │              │  ◀───────────────────────────────  │ _uiState       │
     └──────────────┘   uiState: StateFlow<GameUiState>  │  (privado)     │
                         collectAsStateWithLifecycle()   └────────────────┘
```

- **Los eventos suben** de la vista al ViewModel; **el estado baja** del ViewModel a la vista.
- La vista nunca modifica el estado: solo lo dibuja y notifica eventos.
- `GameUiState` es una `data class` con puros `val`. Para cambiar algo se crea una copia con `copy()`.
- **Backing property:** `_uiState` (`MutableStateFlow`, privado, solo el ViewModel escribe) y
  `uiState` (`StateFlow`, público, de solo lectura).

## Estructura de paquetes

```
com.upchiapas.gato
├── MainActivity.kt                  Monta el tema y GameRoute
├── game
│   ├── model
│   │   ├── Player.kt                enum X / O y next()
│   │   └── GameStatus.kt            Playing(turn) | Won(winner) | Draw
│   └── presentation
│       ├── GameUiState.kt           Estado inmutable de la pantalla
│       ├── GameEvent.kt             Eventos que la UI puede enviar
│       ├── GameViewModel.kt         Toda la lógica del juego
│       ├── GameScreen.kt            GameRoute (conecta VM) + GameScreen (stateless)
│       └── components
│           ├── atoms                PlayerMark, ScoreValue
│           ├── molecules            BoardCell, ScoreCard, StatusBanner
│           └── organisms            GameBoard, Scoreboard
└── ui/theme                         Color, Theme, Type, PlayerColors
```

**Diseño atómico:** los átomos son piezas mínimas (un símbolo, un número animado), las
moléculas combinan átomos (una casilla, una tarjeta del marcador), los organismos combinan
moléculas (el tablero completo, el marcador completo) y la página (`GameScreen`) los acomoda.
Todos los componentes son **stateless**: reciben datos por parámetro y avisan con lambdas.

## Recorrido de un evento, paso a paso (pregunta de control)

Ejemplo: el usuario toca la casilla del centro (índice 4).

1. **`BoardCell`** (molécula): `Surface(onClick = onClick)` detecta el toque y ejecuta la
   lambda `() -> Unit` que recibió. No sabe qué casilla es ni que existe un ViewModel.
2. **`GameBoard`** (organismo): le pasó a esa casilla `onClick = { onCellClick(index) }`,
   así que se agrega el índice `4`.
3. **`GameScreen`** (página): convierte el índice en un evento:
   `onCellClick = { index -> onEvent(GameEvent.CellClicked(index)) }`.
4. **`GameRoute`**: `onEvent = viewModel::onEvent` entrega el evento al ViewModel.
5. **`GameViewModel.onEvent`**: el `when` reconoce `CellClicked` y llama a `playTurn(4)`.
6. **`playTurn`**: dentro de `_uiState.update { current -> ... }` valida la regla de negocio
   (la ronda sigue y la casilla está libre). Si no se cumple, regresa el mismo estado.
7. **`applyMove`**: crea un tablero **nuevo** con `mapIndexed` (no modifica el anterior),
   busca una línea ganadora con `findWinningLine`, decide el nuevo `GameStatus`
   (ganó / empate / turno del otro) y regresa `current.copy(cells, status, score)`.
8. El `MutableStateFlow` **emite** el nuevo `GameUiState` (solo si es distinto al anterior).
9. En `GameRoute`, `collectAsStateWithLifecycle()` recibe el nuevo valor, `uiState` cambia y
   **Compose recompone** `GameScreen`. Solo se vuelven a dibujar los componentes cuyos
   parámetros cambiaron: la casilla 4 (aparece la X con animación) y el `StatusBanner`
   ("Turno de O").

## Cómo se cubre la rúbrica

| Indicador | Dónde se ve |
|---|---|
| Complejidad | `GameViewModel`: transformaciones de colecciones (`mapIndexed`, `firstOrNull`, `all`), 8 líneas ganadoras, turnos alternados, marcador entre rondas |
| Riqueza visual | Scaffold, CenterAlignedTopAppBar, IconButton, Surface, ElevatedCard, Button, AnimatedContent, AnimatedVisibility, `animateColorAsState`, tema propio en `ui/theme` |
| StateFlow | `_uiState` / `uiState`, `collectAsStateWithLifecycle()`, `GameStatus` modela estados claros |
| Composables reutilizables | Atómico en `components/`; todos stateless con state hoisting y lambdas |
| MVVM | Toda decisión de negocio en el ViewModel; el estado sobrevive a la rotación |
| Calidad de código | Paquetes por capa, textos en `strings.xml`, 0 warnings de compilación y de lint, pruebas unitarias |

## Ideas de "modificación express" y dónde se harían

- **Cambiar colores de X u O:** `ui/theme/Color.kt` (o `PlayerColors.kt` para intercambiarlos).
- **Cambiar un texto:** `res/values/strings.xml`.
- **Que siempre empiece X:** en `GameViewModel.startNewRound()` quitar
  `roundStarter = roundStarter.next()`.
- **Contador de movimientos:** agregar `val moves: Int = 0` a `GameUiState`, sumarlo en
  `applyMove` (`moves = current.moves + 1`), reiniciarlo en `startNewRound` y mostrarlo con un `Text`.
- **Bloquear "Nueva ronda" mientras se juega:** `Button(enabled = uiState.status !is GameStatus.Playing, ...)`.

## Ejecutar

- Abrir la carpeta en Android Studio y presionar ▶ Run.
- Pruebas unitarias del ViewModel: `./gradlew test`
