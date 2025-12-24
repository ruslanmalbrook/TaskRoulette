package tech.taskroulette.app.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import tech.taskroulette.app.domain.model.Task
import tech.taskroulette.app.domain.model.TaskSet
import tech.taskroulette.app.domain.random.FakeRandomProvider

class SelectTaskByWeightUseCaseTest {

    @Test
    fun `select returns null when tasks empty`() {
        val useCase = SelectTaskByWeightUseCase(
            randomProvider = FakeRandomProvider(),
        )

        assertNull(useCase.select(emptyList()))
    }

    @Test
    fun `select respects weights using nextLong boundary mapping`() {
        val tasks = listOf(
            Task(id = "a", title = "A", colorArgb = 0, weight = 1, taskSetId = TaskSet.DEFAULT_ID),
            Task(id = "b", title = "B", colorArgb = 0, weight = 3, taskSetId = TaskSet.DEFAULT_ID),
            Task(id = "c", title = "C", colorArgb = 0, weight = 2, taskSetId = TaskSet.DEFAULT_ID),
        )

        // Total weight = 6. Ranges:
        // a: [0]
        // b: [1..3]
        // c: [4..5]
        val useCase = SelectTaskByWeightUseCase(
            randomProvider = FakeRandomProvider(
                longs = ArrayDeque(listOf(0L, 1L, 3L, 4L, 5L)),
            ),
        )

        assertEquals("a", useCase.select(tasks)!!.id)
        assertEquals("b", useCase.select(tasks)!!.id)
        assertEquals("b", useCase.select(tasks)!!.id)
        assertEquals("c", useCase.select(tasks)!!.id)
        assertEquals("c", useCase.select(tasks)!!.id)
    }

    @Test
    fun `select clamps non-positive weights to 1`() {
        val tasks = listOf(
            Task(id = "a", title = "A", colorArgb = 0, weight = 0, taskSetId = TaskSet.DEFAULT_ID),
            Task(id = "b", title = "B", colorArgb = 0, weight = -10, taskSetId = TaskSet.DEFAULT_ID),
        )

        val useCase = SelectTaskByWeightUseCase(
            randomProvider = FakeRandomProvider(
                longs = ArrayDeque(listOf(0L, 1L)),
            ),
        )

        // With clamp, total=2, mapping: 0->a, 1->b.
        assertEquals("a", useCase.select(tasks)!!.id)
        assertEquals("b", useCase.select(tasks)!!.id)
    }
}


