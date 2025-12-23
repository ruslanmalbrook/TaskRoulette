package tech.taskroulette.app.presentation.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.floor
import tech.taskroulette.app.R
import tech.taskroulette.app.presentation.home.HomeEvent
import tech.taskroulette.app.presentation.home.HomeViewModel
import tech.taskroulette.app.presentation.sound.ToneSoundPlayer
import tech.taskroulette.app.presentation.wheel.RouletteWheel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    replaySessionId: String?,
    onEditTasksClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onNavigateToResult: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val rotation = remember { Animatable(0f) }
    val haptic = LocalHapticFeedback.current
    val soundPlayer = remember { ToneSoundPlayer() }
    val autoReplayConsumedSessionId = rememberSaveable { androidx.compose.runtime.mutableStateOf<String?>(null) }

    DisposableEffect(Unit) {
        onDispose { soundPlayer.release() }
    }

    LaunchedEffect(replaySessionId) {
        viewModel.onReplayRequested(replaySessionId)
    }

    LaunchedEffect(state.replaySessionId, state.wheelTasks.size) {
        val id = state.replaySessionId
        if (!id.isNullOrBlank() &&
            id != autoReplayConsumedSessionId.value &&
            state.wheelTasks.isNotEmpty() &&
            !state.isSpinning &&
            state.spinPlan == null
        ) {
            autoReplayConsumedSessionId.value = id
            viewModel.onSpinClick(rotation.value)
        }
        if (id.isNullOrBlank()) {
            autoReplayConsumedSessionId.value = null
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is HomeEvent.NavigateToResult -> onNavigateToResult(event.sessionId)
            }
        }
    }

    LaunchedEffect(state.spinPlan) {
        val plan = state.spinPlan ?: return@LaunchedEffect

        var lastTick = Int.MIN_VALUE
        val tickStep = plan.tickStepDegrees.coerceAtLeast(5f)

        val tickJob = launch {
            snapshotFlow { rotation.value }
                .collectLatest { value ->
                    if (!state.settings.isSoundEnabled && !state.settings.isHapticsEnabled) return@collectLatest

                    val tickIndex = floor(value / tickStep).toInt()
                    if (tickIndex != lastTick) {
                        lastTick = tickIndex
                        if (state.settings.isHapticsEnabled) {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        }
                        if (state.settings.isSoundEnabled && tickIndex % 2 == 0) {
                            soundPlayer.playTick(state.settings.spinSoundTheme)
                        }
                    }
                }
        }

        rotation.animateTo(
            targetValue = plan.targetRotationDegrees,
            animationSpec = tween(
                durationMillis = plan.durationMillis,
                easing = FastOutSlowInEasing,
            ),
        )

        tickJob.cancelAndJoin()
        viewModel.onSpinAnimationFinished()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            state.activeTaskTitle?.let { title ->
                Text(
                    text = stringResource(id = R.string.home_active_task, title),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
            ) {
                RouletteWheel(
                    sectors = state.sectors,
                    rotationDegrees = rotation.value,
                    highlightTaskId = if (state.isSpinning) null else state.highlightTaskId,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Button(
                onClick = { viewModel.onSpinClick(rotation.value) },
                enabled = state.wheelTasks.isNotEmpty() && !state.isSpinning,
            ) {
                Text(text = stringResource(id = R.string.home_spin))
            }

            OutlinedButton(onClick = onEditTasksClick) { Text(text = stringResource(id = R.string.home_edit_tasks)) }
            OutlinedButton(onClick = onHistoryClick) { Text(text = stringResource(id = R.string.home_history)) }
            OutlinedButton(onClick = onSettingsClick) { Text(text = stringResource(id = R.string.home_settings)) }
        }
    }
}


