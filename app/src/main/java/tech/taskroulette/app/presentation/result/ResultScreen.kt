package tech.taskroulette.app.presentation.result

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import tech.taskroulette.app.presentation.components.GradientButton
import tech.taskroulette.app.presentation.components.GradientTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
        containerColor = Transparent,
        topBar = {
            GradientTopAppBar(title = stringResource(id = R.string.result_title))
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
                    .padding(horizontal = 20.dp)
                    .padding(top = 24.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(id = R.string.result_your_task_for_now),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        val colorArgb = state.selectedTaskColorArgb
                        if (colorArgb != null) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(colorArgb)),
                            )
                        }

                        Text(
                            text = state.selectedTaskTitle ?: stringResource(id = R.string.result_task_placeholder),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                }

                AnimatedVisibility(
                    visible = !state.isMarkedDone,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    GradientButton(
                        onClick = viewModel::onMarkDoneClick,
                        text = stringResource(id = R.string.result_mark_done),
                        modifier = Modifier.height(56.dp),
                    )
                }

                AnimatedVisibility(
                    visible = state.isMarkedDone,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        ),
                    ) {
                        Text(
                            text = stringResource(id = R.string.result_marked_done),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    FilledTonalButton(
                        onClick = onBackToHome,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(text = stringResource(id = R.string.result_back_to_home))
                    }
                    FilledTonalButton(
                        onClick = onHistoryClick,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(text = stringResource(id = R.string.home_history))
                    }
                }
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



