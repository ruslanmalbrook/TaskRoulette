package tech.taskroulette.app.domain.usecase.stats

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import tech.taskroulette.app.domain.model.GameSession
import tech.taskroulette.app.domain.model.SessionTaskSnapshot

class ComputeStatsUseCaseTest {

    private val useCase: ComputeStatsUseCase = ComputeStatsUseCase()

    @Test
    fun compute_empty_returnsZeroAndNull(): Unit {
        val stats = useCase.compute(emptyList())
        assertEquals(0, stats.totalSpins)
        assertNull(stats.mostFrequentTaskTitle)
    }

    @Test
    fun compute_tieBreak_isDeterministicByKeyAsc(): Unit {
        val sessionA = GameSession(
            id = "s1",
            createdAtEpochMillis = 1L,
            selectedSnapshotTaskId = "s1-a",
            tasksSnapshot = listOf(
                snapshot(
                    id = "s1-a",
                    originalTaskId = "a",
                    title = "Task A",
                ),
            ),
        )
        val sessionB = GameSession(
            id = "s2",
            createdAtEpochMillis = 2L,
            selectedSnapshotTaskId = "s2-b",
            tasksSnapshot = listOf(
                snapshot(
                    id = "s2-b",
                    originalTaskId = "b",
                    title = "Task B",
                ),
            ),
        )

        val stats = useCase.compute(listOf(sessionB, sessionA))

        assertEquals(2, stats.totalSpins)
        assertEquals("Task A", stats.mostFrequentTaskTitle)
    }

    private fun snapshot(
        id: String,
        originalTaskId: String?,
        title: String,
    ): SessionTaskSnapshot = SessionTaskSnapshot(
        id = id,
        originalTaskId = originalTaskId,
        title = title,
        colorArgb = 0,
        weight = 1,
        orderIndex = 0,
    )
}


