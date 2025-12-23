package tech.taskroulette.app.presentation

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.*
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tech.taskroulette.app.MainActivity
import tech.taskroulette.app.R
import tech.taskroulette.app.data.local.db.TaskRouletteDatabase

@RunWith(AndroidJUnit4::class)
class AppFlowSmokeTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun addTask_andSpin_showsResult(): Unit {
        clearDatabase()

        val context = composeRule.activity
        val editTasks = context.getString(R.string.home_edit_tasks)
        val add = context.getString(R.string.task_editor_add)
        val titleLabel = context.getString(R.string.task_editor_title_label)
        val save = context.getString(R.string.action_save)
        val back = context.getString(R.string.action_back)
        val spin = context.getString(R.string.home_spin)
        val resultTitle = context.getString(R.string.result_your_task_for_now)

        val taskTitle = "Smoke Test Task"

        composeRule.onNodeWithText(editTasks).performClick()
        composeRule.onNodeWithText(add).performClick()
        composeRule.onNodeWithText(titleLabel).performTextInput(taskTitle)
        composeRule.onNodeWithText(save).performClick()
        composeRule.onNodeWithText(back).performClick()

        composeRule.onNodeWithText(spin).assertIsEnabled().performClick()

        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithText(resultTitle).fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText(resultTitle).assertExists()
        composeRule.onNodeWithText(taskTitle).assertExists()
    }

    private fun clearDatabase(): Unit {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val db = Room.databaseBuilder(context, TaskRouletteDatabase::class.java, "taskroulette.db")
            .allowMainThreadQueries()
            .build()
        db.clearAllTables()
        db.close()
    }
}


