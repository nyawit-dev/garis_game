package ngga.ring.tetris.utils

import ngga.ring.tetris.type.Direction

fun Offset(x: Int, y: Int) = androidx.compose.ui.geometry.Offset(x.toFloat(), y.toFloat())

fun Direction.toOffset() = when (this) {
    Direction.Left -> -1 to 0
    Direction.Up -> 0 to -1
    Direction.Right -> 1 to 0
    Direction.Down -> 0 to 1
}

val NextMatrix = 4 to 2
const val ScoreEverySpirit = 12

fun calculateScore(lines: Int) = when (lines) {
    1 -> 100
    2 -> 300
    3 -> 700
    4 -> 1500
    else -> 0
}
