package tech.taskroulette.app.presentation.taskeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.math.roundToInt
import tech.taskroulette.app.R
import tech.taskroulette.app.domain.model.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditorScreen(
    onBack: () -> Unit,
    viewModel: TaskEditorViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.task_editor_title)) },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text(text = stringResource(id = R.string.action_back)) }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = viewModel::onAddTaskClick,
            ) {
                Text(text = stringResource(id = R.string.task_editor_add))
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (state.tasks.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.task_editor_empty),
                    style = MaterialTheme.typography.bodyLarge,
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(
                        items = state.tasks,
                        key = { it.id },
                    ) { task ->
                        TaskRow(
                            task = task,
                            onEditClick = { viewModel.onEditTaskClick(task) },
                            onDeleteClick = { viewModel.onDeleteTaskClick(task.id) },
                        )
                    }
                }
            }
        }
    }

    state.editDialog?.let { dialog ->
        TaskEditDialog(
            dialog = dialog,
            availableColors = viewModel.availableColors(),
            onDismiss = viewModel::onDialogDismiss,
            onTitleChange = viewModel::onDialogTitleChange,
            onWeightChange = viewModel::onDialogWeightChange,
            onColorChange = viewModel::onDialogColorChange,
            onSave = viewModel::onDialogSaveClick,
        )
    }
}

@Composable
private fun TaskRow(
    task: Task,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onEditClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(MaterialTheme.shapes.small)
                .background(Color(task.colorArgb)),
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = stringResource(id = R.string.task_editor_weight_value, task.weight),
                style = MaterialTheme.typography.bodySmall,
            )
        }

        OutlinedButton(onClick = onDeleteClick) {
            Text(text = stringResource(id = R.string.action_delete))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskEditDialog(
    dialog: TaskEditDialogState,
    availableColors: List<Int>,
    onDismiss: () -> Unit,
    onTitleChange: (String) -> Unit,
    onWeightChange: (Int) -> Unit,
    onColorChange: (Int) -> Unit,
    onSave: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (dialog.existingTaskId == null) {
                    stringResource(id = R.string.task_editor_add_dialog_title)
                } else {
                    stringResource(id = R.string.task_editor_edit_dialog_title)
                },
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedTextField(
                    value = dialog.title,
                    onValueChange = onTitleChange,
                    label = { Text(text = stringResource(id = R.string.task_editor_title_label)) },
                    isError = dialog.isTitleError,
                    singleLine = true,
                )
                if (dialog.isTitleError) {
                    Text(
                        text = stringResource(id = R.string.task_editor_title_error),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(id = R.string.task_editor_weight_label),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Slider(
                    value = dialog.weight.toFloat(),
                    onValueChange = { onWeightChange(it.roundToInt()) },
                    valueRange = 1f..10f,
                    steps = 8,
                )
                Text(
                    text = stringResource(id = R.string.task_editor_weight_value, dialog.weight),
                    style = MaterialTheme.typography.bodySmall,
                )

                Text(
                    text = stringResource(id = R.string.task_editor_color_label),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    availableColors.forEach { colorArgb ->
                        val isSelected = colorArgb == dialog.colorArgb
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 28.dp else 24.dp)
                                .clip(MaterialTheme.shapes.small)
                                .background(Color(colorArgb))
                                .clickable { onColorChange(colorArgb) },
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onSave) { Text(text = stringResource(id = R.string.action_save)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(text = stringResource(id = R.string.action_cancel)) }
        },
    )
}


