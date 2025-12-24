package tech.taskroulette.app.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import tech.taskroulette.app.domain.IdGenerator
import tech.taskroulette.app.domain.model.Task
import tech.taskroulette.app.domain.model.TaskSet

class CreateGameSessionUseCaseTest {

    private class FakeIdGenerator(
        private val ids: ArrayDeque<String>,
    ) : IdGenerator {
        override fun newId(): String = ids.removeFirst()
    }

    @Test
    fun `createFromTasks creates snapshot tasks and maps selectedSnapshotTaskId`() {
        val useCase = CreateGameSessionUseCase(
            idGenerator = FakeIdGenerator(
                ids = ArrayDeque(
                    listOf(
                        "session-1",
                        "snap-1",
                        "snap-2",
                    ),
                ),
            ),
        )

        val tasks = listOf(
            Task(id = "t1", title = "Task 1", colorArgb = 123, weight = 1, taskSetId = TaskSet.DEFAULT_ID),
            Task(id = "t2", title = "Task 2", colorArgb = 456, weight = 2, taskSetId = TaskSet.DEFAULT_ID),
        )

        val session = useCase.createFromTasks(
            tasks = tasks,
            selectedTaskId = "t2",
            createdAtEpochMillis = 111L,
        )

        assertEquals("session-1", session.id)
        assertEquals(111L, session.createdAtEpochMillis)
        assertEquals(2, session.tasksSnapshot.size)

        // Snapshot ids come from generator in order.
        assertEquals("snap-1", session.tasksSnapshot[0].id)
        assertEquals("snap-2", session.tasksSnapshot[1].id)

        // Snapshot keeps original task ids and order.
        assertEquals("t1", session.tasksSnapshot[0].originalTaskId)
        assertEquals(0, session.tasksSnapshot[0].orderIndex)
        assertEquals("t2", session.tasksSnapshot[1].originalTaskId)
        assertEquals(1, session.tasksSnapshot[1].orderIndex)

        // Selected snapshot id points to snapshot record (not original task id).
        assertEquals("snap-2", session.selectedSnapshotTaskId)

        val selected = session.tasksSnapshot.firstOrNull { it.id == session.selectedSnapshotTaskId }
        assertNotNull(selected)
        assertEquals("t2", selected!!.originalTaskId)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `createFromTasks throws if selectedTaskId not in list`() {
        val useCase = CreateGameSessionUseCase(
            idGenerator = FakeIdGenerator(
                ids = ArrayDeque(
                    listOf(
                        "session-1",
                        "snap-1",
                    ),
                ),
            ),
        )

        val tasks = listOf(
            Task(id = "t1", title = "Task 1", colorArgb = 0, weight = 1, taskSetId = TaskSet.DEFAULT_ID),
        )

        useCase.createFromTasks(
            tasks = tasks,
            selectedTaskId = "missing",
            createdAtEpochMillis = 0L,
        )
    }
}


