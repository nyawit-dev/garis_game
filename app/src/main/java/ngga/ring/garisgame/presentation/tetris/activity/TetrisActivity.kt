package ngga.ring.garisgame.presentation.tetris.activity

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import ngga.ring.core.ui.theme.GarisGameTheme
import ngga.ring.garisgame.presentation.tetris.screen.GameScreen
import ngga.ring.garisgame.presentation.tetris.screen.PreviewGamescreen
import ngga.ring.garisgame.presentation.tetris.view_models.Action
import ngga.ring.garisgame.presentation.tetris.view_models.GameViewModel
import ngga.ring.tetris.type.Direction
import ngga.ring.tetris.ui.GameBody
import ngga.ring.tetris.ui.combinedClickable
import ngga.ring.tetris.utils.SoundUtil
import ngga.ring.tetris.utils.StatusBarUtil

class TetrisActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        StatusBarUtil.transparentStatusBar(this)
        SoundUtil.init(this)
        setContent {
            GarisGameTheme {
                Surface(color = MaterialTheme.colorScheme.background) {

                    val viewModel = viewModel<GameViewModel>()
                    val viewState = viewModel.viewState.value

                    LaunchedEffect(key1 = Unit) {
                        while (isActive) {
                            delay(650L - 55 * (viewState.level - 1))
                            viewModel.dispatch(Action.GameTick)
                        }
                    }

                    val lifecycleOwner = LocalLifecycleOwner.current
                    DisposableEffect(key1 = Unit) {
                        val observer = object : DefaultLifecycleObserver {
                            override fun onResume(owner: LifecycleOwner) {
                                viewModel.dispatch(Action.Resume)
                            }

                            override fun onPause(owner: LifecycleOwner) {
                                viewModel.dispatch(Action.Pause)
                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)
                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }


                    GameBody(combinedClickable(
                        onMove = { direction: Direction ->
                            if (direction == Direction.Up) viewModel.dispatch(Action.Drop)
                            else viewModel.dispatch(Action.Move(direction))
                        },
                        onRotate = {
                            viewModel.dispatch(Action.Rotate)
                        },
                        onRestart = {
                            viewModel.dispatch(Action.Reset)
                        },
                        onPause = {
                            if (viewModel.viewState.value.isRuning) {
                                viewModel.dispatch(Action.Pause)
                            } else {
                                viewModel.dispatch(Action.Resume)
                            }
                        },
                        onMute = {
                            viewModel.dispatch(Action.Mute)
                        }
                    )) {
                        GameScreen(
                            Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SoundUtil.release()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GarisGameTheme {
        GameBody {
            PreviewGamescreen(Modifier.fillMaxSize())
        }
    }
}
