package ngga.ring.tetris.type

import ngga.ring.tetris.R

sealed class SoundType(val res: Int) {
    object Move : SoundType(R.raw.move)
    object Rotate : SoundType(R.raw.rotate)
    object Start : SoundType(R.raw.start)
    object Drop : SoundType(R.raw.drop)
    object Clean : SoundType(R.raw.clean)
}
