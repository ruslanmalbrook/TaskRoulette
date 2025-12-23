package tech.taskroulette.app.domain.usecase.spin

import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.min
import tech.taskroulette.app.domain.model.Task
import tech.taskroulette.app.domain.random.RandomProvider
import tech.taskroulette.app.domain.usecase.BuildWheelSectorsUseCase
import tech.taskroulette.app.domain.usecase.SelectTaskByWeightUseCase

class PlanSpinUseCase @Inject constructor(
    private val randomProvider: RandomProvider,
    private val buildWheelSectorsUseCase: BuildWheelSectorsUseCase,
    private val selectTaskByWeightUseCase: SelectTaskByWeightUseCase,
) {
    fun plan(
        tasks: List<Task>,
        currentRotationDegrees: Float,
    ): SpinPlan? {
        if (tasks.isEmpty()) return null

        val sectors = buildWheelSectorsUseCase.build(tasks)
        val selectedTask = selectTaskByWeightUseCase.select(tasks) ?: return null
        val selectedSector = sectors.firstOrNull { it.task.id == selectedTask.id } ?: return null

        val sweep = selectedSector.sweepAngleDegrees
        val margin = min(6.0, sweep * 0.12).coerceAtLeast(0.0)
        val usable = (sweep - 2.0 * margin).coerceAtLeast(0.0)

        val offsetInSector = if (usable <= 0.0) {
            sweep / 2.0
        } else {
            val r = randomProvider.nextLong(1_000_000L).toDouble() / 1_000_000.0
            margin + r * usable
        }

        val angleInWheel = selectedSector.startAngleDegrees + offsetInSector

        val currentNorm = normalizeDegrees(currentRotationDegrees.toDouble())
        val targetNorm = normalizeDegrees(POINTER_ANGLE_DEGREES - angleInWheel)
        val delta = normalizeDegrees(targetNorm - currentNorm)

        val extraTurns = MIN_TURNS + randomProvider.nextInt(MAX_TURNS - MIN_TURNS + 1)
        val finalRotation = currentRotationDegrees + (extraTurns * 360.0 + delta).toFloat()

        val duration = MIN_DURATION_MS + randomProvider.nextInt(MAX_DURATION_MS - MIN_DURATION_MS + 1)

        return SpinPlan(
            selectedTask = selectedTask,
            targetRotationDegrees = finalRotation,
            durationMillis = duration,
            tickStepDegrees = DEFAULT_TICK_STEP_DEGREES,
        )
    }

    private fun normalizeDegrees(value: Double): Double {
        var v = value % 360.0
        if (v < 0.0) v += 360.0
        // Reason: avoid tiny negative zeros and float drift in tests/logic.
        return if (abs(v) < 1e-9) 0.0 else v
    }

    companion object {
        private const val POINTER_ANGLE_DEGREES: Double = -90.0

        private const val MIN_TURNS: Int = 3
        private const val MAX_TURNS: Int = 6

        private const val MIN_DURATION_MS: Int = 2400
        private const val MAX_DURATION_MS: Int = 4200

        private const val DEFAULT_TICK_STEP_DEGREES: Float = 20f
    }
}


