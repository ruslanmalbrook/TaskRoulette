package tech.taskroulette.app.data.repository

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import tech.taskroulette.app.data.local.db.TaskRouletteDatabase
import tech.taskroulette.app.domain.model.Task

@RunWith(AndroidJUnit4::class)
class TaskRepositoryImplTest {

    private lateinit var db: TaskRouletteDatabase
    private lateinit var repository: TaskRepositoryImpl

    @Before
    fun setUp(): Unit {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, TaskRouletteDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = TaskRepositoryImpl(db.taskDao())
    }

    @After
    fun tearDown(): Unit {
        db.close()
    }

    @Test
    fun upsert_delete_observeOrdersByTitleCaseInsensitive(): Unit = runBlocking {
        repository.upsert(
            Task(
                id = "2",
                title = "B task",
                colorArgb = 0,
                weight = 1,
            ),
        )
        repository.upsert(
            Task(
                id = "1",
                title = "a task",
                colorArgb = 0,
                weight = 1,
            ),
        )

        val observed = repository.observeTasks().first()
        assertEquals(listOf("a task", "B task"), observed.map { it.title })

        repository.delete("1")
        val remaining = repository.getTasks()
        assertEquals(listOf("B task"), remaining.map { it.title })
    }
}


