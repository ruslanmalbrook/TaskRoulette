package tech.taskroulette.app.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import tech.taskroulette.app.domain.model.TaskSet
import tech.taskroulette.app.domain.model.Task

class BuildWheelSectorsUseCaseTest {

    @Test
    fun `build returns empty list when tasks empty`() {
        val useCase = BuildWheelSectorsUseCase()
        assertTrue(useCase.build(emptyList()).isEmpty())
    }

    @Test
    fun `build returns one sector of 360 degrees when single task`() {
        val useCase = BuildWheelSectorsUseCase()
        val sectors = useCase.build(
            listOf(Task(id = "a", title = "A", colorArgb = 0, weight = 1, taskSetId = TaskSet.DEFAULT_ID)),
        )

        assertEquals(1, sectors.size)
        assertEquals(0.0, sectors.first().startAngleDegrees, 0.0)
        assertEquals(360.0, sectors.first().sweepAngleDegrees, 0.0)
    }

    @Test
    fun `build computes angles proportional to weights and sums to 360`() {
        val useCase = BuildWheelSectorsUseCase()
        val sectors = useCase.build(
            listOf(
                Task(id = "a", title = "A", colorArgb = 0, weight = 1, taskSetId = TaskSet.DEFAULT_ID),
                Task(id = "b", title = "B", colorArgb = 0, weight = 2, taskSetId = TaskSet.DEFAULT_ID),
                Task(id = "c", title = "C", colorArgb = 0, weight = 3, taskSetId = TaskSet.DEFAULT_ID),
            ),
        )

        assertEquals(3, sectors.size)

        // Expected: 60, 120, 180
        assertEquals(0.0, sectors[0].startAngleDegrees, 1e-9)
        assertEquals(60.0, sectors[0].sweepAngleDegrees, 1e-6)

        assertEquals(60.0, sectors[1].startAngleDegrees, 1e-6)
        assertEquals(120.0, sectors[1].sweepAngleDegrees, 1e-6)

        assertEquals(180.0, sectors[2].startAngleDegrees, 1e-6)
        assertEquals(180.0, sectors[2].sweepAngleDegrees, 1e-6)

        val sum = sectors.sumOf { it.sweepAngleDegrees }
        assertEquals(360.0, sum, 1e-6)
    }

    @Test
    fun `build clamps non-positive weights to 1`() {
        val useCase = BuildWheelSectorsUseCase()
        val sectors = useCase.build(
            listOf(
                Task(id = "a", title = "A", colorArgb = 0, weight = 0, taskSetId = TaskSet.DEFAULT_ID),
                Task(id = "b", title = "B", colorArgb = 0, weight = -10, taskSetId = TaskSet.DEFAULT_ID),
            ),
        )

        // With clamp, both weights are 1 => 180 / 180
        assertEquals(0.0, sectors[0].startAngleDegrees, 1e-9)
        assertEquals(180.0, sectors[0].sweepAngleDegrees, 1e-6)
        assertEquals(180.0, sectors[1].startAngleDegrees, 1e-6)
        assertEquals(180.0, sectors[1].sweepAngleDegrees, 1e-6)
        assertEquals(360.0, sectors.sumOf { it.sweepAngleDegrees }, 1e-6)
    }
}


