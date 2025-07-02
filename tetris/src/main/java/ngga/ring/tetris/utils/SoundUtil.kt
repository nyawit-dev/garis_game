package ngga.ring.tetris.utils

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import ngga.ring.tetris.type.SoundType

@SuppressLint("StaticFieldLeak")
object SoundUtil {

    private var _context: Context? = null
    private val sp: SoundPool by lazy {
        SoundPool.Builder().setMaxStreams(4).setMaxStreams(AudioManager.STREAM_MUSIC).build()
    }
    private val _map = mutableMapOf<SoundType, Int>()

    fun init(context: Context) {
        _context = context
        Sounds.forEach {
            _map[it] = sp.load(_context, it.res, 1)
        }
    }

    fun release() {
        _context = null
        sp.release()
    }


    fun play(isMute: Boolean, sound: SoundType) {
        if (!isMute) {
            sp.play(requireNotNull(_map[sound]), 1f, 1f, 0, 0, 1f)
        }
    }

}