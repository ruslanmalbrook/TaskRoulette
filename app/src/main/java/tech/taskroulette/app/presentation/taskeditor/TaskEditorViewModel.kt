package tech.taskroulette.app.presentation.taskeditor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.taskroulette.app.domain.model.Task
import tech.taskroulette.app.domain.usecase.task.DeleteTaskUseCase
import tech.taskroulette.app.domain.usecase.task.GenerateTaskColorUseCase
import tech.taskroulette.app.domain.usecase.task.ObserveTasksUseCase
import tech.taskroulette.app.domain.usecase.task.SaveTaskUseCase

@HiltViewModel
class TaskEditorViewModel @Inject constructor(
    observeTasksUseCase: ObserveTasksUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val generateTaskColorUseCase: GenerateTaskColorUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<TaskEditorUiState> = MutableStateFlow(TaskEditorUiState())
    val state: StateFlow<TaskEditorUiState> = _state.asStateFlow()

    init {
        observeTasksUseCase()
            .onEach { tasks ->
                _state.update { current ->
                    current.copy(tasks = tasks)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAddTaskClick(): Unit {
        val color = generateTaskColorUseCase.generate(_state.value.tasks.size)
        _state.update { current ->
            current.copy(
                editDialog = TaskEditDialogState(
                    existingTaskId = null,
                    title = "",
                    weight = 1,
                    colorArgb = color,
                    isTitleError = false,
                ),
            )
        }
    }

    fun onEditTaskClick(task: Task): Unit {
        _state.update { current ->
            current.copy(
                editDialog = TaskEditDialogState(
                    existingTaskId = task.id,
                    title = task.title,
                    weight = task.weight,
                    colorArgb = task.colorArgb,
                    isTitleError = false,
                ),
            )
        }
    }

    fun onDeleteTaskClick(taskId: String): Unit {
        viewModelScope.launch {
            deleteTaskUseCase(taskId)
        }
    }

    fun onDialogDismiss(): Unit {
        _state.update { current -> current.copy(editDialog = null) }
    }

    fun onDialogTitleChange(title: String): Unit {
        _state.update { current ->
            current.copy(
                editDialog = current.editDialog?.copy(
                    title = title,
                    isTitleError = false,
                ),
            )
        }
    }

    fun onDialogWeightChange(weight: Int): Unit {
        _state.update { current ->
            current.copy(
                editDialog = current.editDialog?.copy(
                    weight = weight.coerceAtLeast(1),
                ),
            )
        }
    }

    fun onDialogColorChange(colorArgb: Int): Unit {
        _state.update { current ->
            current.copy(
                editDialog = current.editDialog?.copy(
                    colorArgb = colorArgb,
                ),
            )
        }
    }

    fun onDialogSaveClick(): Unit {
        val dialog = _state.value.editDialog ?: return
        if (dialog.title.isBlank()) {
            _state.update { current ->
                current.copy(
                    editDialog = dialog.copy(isTitleError = true),
                )
            }
            return
        }

        viewModelScope.launch {
            saveTaskUseCase.execute(
                existingTaskId = dialog.existingTaskId,
                title = dialog.title,
                colorArgb = dialog.colorArgb,
                weight = dialog.weight,
            )
            onDialogDismiss()
        }
    }

    fun availableColors(): List<Int> = generateTaskColorUseCase.palette(size = 10)
}

data class TaskEditorUiState(
    val tasks: List<Task> = emptyList(),
    val editDialog: TaskEditDialogState? = null,
)

data class TaskEditDialogState(
    val existingTaskId: String?,
    val title: String,
    val weight: Int,
    val colorArgb: Int,
    val isTitleError: Boolean,
)


