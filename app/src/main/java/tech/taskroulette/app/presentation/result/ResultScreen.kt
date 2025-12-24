package tech.taskroulette.app.presentation.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tech.taskroulette.app.R
import tech.taskroulette.app.presentation.confetti.ConfettiOverlay
import tech.taskroulette.app.presentation.sound.ToneSoundPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    sessionId: String?,
    onBack: () -> Unit,
    onBackToHome: () -> Unit,
    onHistoryClick: () -> Unit,
    viewModel: ResultViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val haptic = LocalHapticFeedback.current
    val soundPlayer = rememberToneSoundPlayer()

    LaunchedEffect(state.sessionId) {
        if (state.sessionId.isNullOrBlank()) return@LaunchedEffect
        if (state.settings.isHapticsEnabled) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
        if (state.settings.isSoundEnabled) {
            soundPlayer.playStop(state.settings.spinSoundTheme)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.result_title)) },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text(text = stringResource(id = R.string.action_back)) }
                },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.result_your_task_for_now),
                    style = MaterialTheme.typography.headlineSmall,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    val colorArgb = state.selectedTaskColorArgb
                    if (colorArgb != null) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(MaterialTheme.shapes.small)
                                .background(Color(colorArgb)),
                        )
                    }

                    Text(
                        text = state.selectedTaskTitle ?: stringResource(id = R.string.result_task_placeholder),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (!state.isMarkedDone) {
                    OutlinedButton(
                        onClick = viewModel::onMarkDoneClick,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(text = stringResource(id = R.string.result_mark_done))
                    }
                } else {
                    Text(
                        text = stringResource(id = R.string.result_marked_done),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                OutlinedButton(onClick = onBackToHome) { Text(text = stringResource(id = R.string.result_back_to_home)) }
                OutlinedButton(onClick = onHistoryClick) { Text(text = stringResource(id = R.string.home_history)) }
            }

            ConfettiOverlay(
                isRunning = !state.sessionId.isNullOrBlank(),
                style = state.confettiStyle,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun rememberToneSoundPlayer(): ToneSoundPlayer {
    val player = androidx.compose.runtime.remember { ToneSoundPlayer() }
    DisposableEffect(Unit) {
        onDispose { player.release() }
    }
    return player
}



