package tech.taskroulette.app.presentation.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.unit.dp
import tech.taskroulette.app.presentation.components.GradientButton
import tech.taskroulette.app.presentation.components.GradientTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.floor
import tech.taskroulette.app.R
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
    val wheelA11yDescription = stringResource(
        id = R.string.a11y_wheel_description,
        state.wheelTasks.size,
    )
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
        containerColor = Transparent,
        topBar = {
            GradientTopAppBar(title = stringResource(id = R.string.app_name))
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            AnimatedVisibility(
                visible = state.activeTaskTitle != null,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                state.activeTaskTitle?.let { title ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                        ),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MaterialTheme.colorScheme.primary),
                            )
                            Text(
                                text = stringResource(id = R.string.home_active_task, title),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    .padding(8.dp)
                    .pointerInput(state.wheelTasks.isNotEmpty(), state.isSpinning) {
                        if (state.wheelTasks.isEmpty() || state.isSpinning) return@pointerInput

                        var totalDrag = Offset.Zero
                        detectDragGestures(
                            onDragEnd = {
                                val distance = totalDrag.getDistance()
                                // Reason: Thresholds for gesture spin: 100dp distance
                                val distanceThreshold = 100.dp.toPx()

                                if (distance > distanceThreshold) {
                                    viewModel.onSpinClick(rotation.value)
                                }
                                totalDrag = Offset.Zero
                            },
                        ) { change, dragAmount ->
                            totalDrag += dragAmount
                        }
                    }
                    .semantics {
                        // Reason: semantics {} is not a composable scope.
                        contentDescription = wheelA11yDescription
                    },
            ) {
                RouletteWheel(
                    sectors = state.sectors,
                    rotationDegrees = rotation.value,
                    highlightTaskId = if (state.isSpinning) null else state.highlightTaskId,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            GradientButton(
                onClick = { viewModel.onSpinClick(rotation.value) },
                enabled = state.wheelTasks.isNotEmpty() && !state.isSpinning,
                text = stringResource(id = R.string.home_spin),
                modifier = Modifier.height(56.dp),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                FilledTonalButton(
                    onClick = onEditTasksClick,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = stringResource(id = R.string.home_edit_tasks))
                }
                FilledTonalButton(
                    onClick = onHistoryClick,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = stringResource(id = R.string.home_history))
                }
                FilledTonalButton(
                    onClick = onSettingsClick,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = stringResource(id = R.string.home_settings))
                }
            }
        }
    }
}



