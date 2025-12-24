package tech.taskroulette.app.domain.usecase.spin

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import tech.taskroulette.app.domain.model.Task
import tech.taskroulette.app.domain.model.TaskSet
import tech.taskroulette.app.domain.random.FakeRandomProvider
import tech.taskroulette.app.domain.usecase.BuildWheelSectorsUseCase
import tech.taskroulette.app.domain.usecase.SelectTaskByWeightUseCase
import kotlin.math.abs
import kotlin.math.min

class PlanSpinUseCaseTest {

    @Test
    fun plan_stopsExactlyOnSelectedSector(): Unit {
        val random = FakeRandomProvider(
            ints = ArrayDeque(listOf(0, 0)),
            longs = ArrayDeque(listOf(0L, 0L)),
        )

        val buildSectors = BuildWheelSectorsUseCase()
        val selectTask = SelectTaskByWeightUseCase(random)
        val useCase = PlanSpinUseCase(
            randomProvider = random,
            buildWheelSectorsUseCase = buildSectors,
            selectTaskByWeightUseCase = selectTask,
        )

        val tasks = listOf(
            Task(id = "1", title = "A", colorArgb = 0, weight = 1, taskSetId = TaskSet.DEFAULT_ID),
            Task(id = "2", title = "B", colorArgb = 0, weight = 1, taskSetId = TaskSet.DEFAULT_ID),
        )

        val plan = useCase.plan(tasks = tasks, currentRotationDegrees = 0f)
        assertNotNull(plan)

        val selectedTaskId = plan!!.selectedTask.id
        val sectors = buildSectors.build(tasks)
        val selectedSector = sectors.first { it.task.id == selectedTaskId }

        val sweep = selectedSector.sweepAngleDegrees
        val margin = min(6.0, sweep * 0.12).coerceAtLeast(0.0)
        val offsetInSector = margin // Reason: fake random provides 0L for offset.
        val angleInWheel = selectedSector.startAngleDegrees + offsetInSector

        val expectedRotationNorm = normalizeDegrees(POINTER_ANGLE_DEGREES - angleInWheel)
        val actualRotationNorm = normalizeDegrees(plan.targetRotationDegrees.toDouble())

        assertEquals(expectedRotationNorm, actualRotationNorm, 1e-6)
    }

    private fun normalizeDegrees(value: Double): Double {
        var v = value % 360.0
        if (v < 0.0) v += 360.0
        return if (abs(v) < 1e-9) 0.0 else v
    }

    private companion object {
        private const val POINTER_ANGLE_DEGREES: Double = -90.0
    }
}


