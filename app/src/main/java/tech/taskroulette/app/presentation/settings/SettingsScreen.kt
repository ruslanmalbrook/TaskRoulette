package tech.taskroulette.app.presentation.settings

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tech.taskroulette.app.R
import tech.taskroulette.app.domain.model.ConfettiStyle
import tech.taskroulette.app.domain.model.SpinSoundTheme
import tech.taskroulette.app.domain.model.TaskSet
import tech.taskroulette.app.domain.usecase.taskimport.ImportResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json"),
    ) { uri: Uri? ->
        if (uri != null) {
            val taskSetId = state.settings.activeTaskSetId
            scope.launch {
                try {
                    val jsonContent = viewModel.exportToJson(taskSetId)
                    withContext(Dispatchers.IO) {
                        context.contentResolver.openOutputStream(uri)?.use { output ->
                            output.write(jsonContent.toByteArray())
                        }
                    }
                } catch (e: Exception) {
                    // Reason: Error handling is done via state
                }
            }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch {
                try {
                    val jsonContent = withContext(Dispatchers.IO) {
                        context.contentResolver.openInputStream(uri)?.use { input ->
                            input.bufferedReader().use { it.readText() }
                        } ?: throw IllegalStateException("Failed to read file")
                    }
                    val result = viewModel.importFromJson(jsonContent, createNewSet = false)
                    viewModel.onImportResult(result)
                } catch (e: Exception) {
                    viewModel.onImportResult(
                        ImportResult.Error("Import failed: ${e.message}"),
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.settings_title)) },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text(text = stringResource(id = R.string.action_back)) }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = stringResource(id = R.string.settings_sound))
                Switch(
                    checked = state.settings.isSoundEnabled,
                    onCheckedChange = viewModel::onSoundEnabledChange,
                )
            }

            Text(
                text = stringResource(id = R.string.settings_sound_theme),
                style = MaterialTheme.typography.titleMedium,
            )
            SoundThemeRow(
                theme = SpinSoundTheme.Classic,
                selected = state.settings.spinSoundTheme,
                onSelect = viewModel::onSoundThemeChange,
                label = stringResource(id = R.string.settings_sound_theme_classic),
            )
            SoundThemeRow(
                theme = SpinSoundTheme.Soft,
                selected = state.settings.spinSoundTheme,
                onSelect = viewModel::onSoundThemeChange,
                label = stringResource(id = R.string.settings_sound_theme_soft),
            )
            SoundThemeRow(
                theme = SpinSoundTheme.Arcade,
                selected = state.settings.spinSoundTheme,
                onSelect = viewModel::onSoundThemeChange,
                label = stringResource(id = R.string.settings_sound_theme_arcade),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = stringResource(id = R.string.settings_haptics))
                Switch(
                    checked = state.settings.isHapticsEnabled,
                    onCheckedChange = viewModel::onHapticsEnabledChange,
                )
            }

            Text(
                text = stringResource(id = R.string.settings_confetti_style),
                style = MaterialTheme.typography.titleMedium,
            )
            ConfettiStyleRow(
                style = ConfettiStyle.Classic,
                selected = state.settings.confettiStyle,
                onSelect = viewModel::onConfettiStyleChange,
                label = stringResource(id = R.string.settings_confetti_style_classic),
            )
            ConfettiStyleRow(
                style = ConfettiStyle.Pop,
                selected = state.settings.confettiStyle,
                onSelect = viewModel::onConfettiStyleChange,
                label = stringResource(id = R.string.settings_confetti_style_pop),
            )
            ConfettiStyleRow(
                style = ConfettiStyle.Streamers,
                selected = state.settings.confettiStyle,
                onSelect = viewModel::onConfettiStyleChange,
                label = stringResource(id = R.string.settings_confetti_style_streamers),
            )

            Text(
                text = stringResource(id = R.string.settings_stats_title),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = stringResource(id = R.string.settings_stats_total, state.stats.totalSpins),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = stringResource(
                    id = R.string.settings_stats_most_frequent,
                    state.stats.mostFrequentTaskTitle ?: stringResource(id = R.string.placeholder_dash),
                ),
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = stringResource(id = R.string.settings_progress_title),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = stringResource(id = R.string.settings_progress_current_streak, state.progress.currentStreak),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = stringResource(id = R.string.settings_progress_longest_streak, state.progress.longestStreak),
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = stringResource(id = R.string.settings_task_sets_title),
                style = MaterialTheme.typography.titleMedium,
            )
            val activeTaskSetName = state.taskSets
                .firstOrNull { it.id == state.settings.activeTaskSetId }
                ?.name
                ?: stringResource(id = R.string.placeholder_dash)
            Text(
                text = stringResource(id = R.string.settings_task_sets_active, activeTaskSetName),
                style = MaterialTheme.typography.bodyMedium,
            )
            if (state.taskSets.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.settings_task_sets_empty),
                    style = MaterialTheme.typography.bodyMedium,
                )
            } else {
                state.taskSets.forEach { taskSet ->
                    TaskSetRow(
                        taskSet = taskSet,
                        isSelected = taskSet.id == state.settings.activeTaskSetId,
                        onSelect = viewModel::onTaskSetSelect,
                    )
                }
            }
            OutlinedButton(
                onClick = viewModel::onAddTaskSetClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(id = R.string.settings_task_sets_add))
            }

            Text(
                text = stringResource(id = R.string.settings_import_export_title),
                style = MaterialTheme.typography.titleMedium,
            )
            OutlinedButton(
                onClick = {
                    val taskSetName = state.taskSets
                        .firstOrNull { it.id == state.settings.activeTaskSetId }
                        ?.name
                        ?: "tasks"
                    exportLauncher.launch("taskroulette_${taskSetName}_${System.currentTimeMillis()}.json")
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(id = R.string.settings_export))
            }
            OutlinedButton(
                onClick = {
                    importLauncher.launch(arrayOf("application/json", "text/*"))
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(id = R.string.settings_import))
            }
        }
    }


    state.importResult?.let { result ->
        AlertDialog(
            onDismissRequest = viewModel::onImportResultDismissed,
            title = { Text(text = stringResource(id = R.string.settings_import_dialog_title)) },
            text = {
                when (result) {
                    is ImportResult.Success -> {
                        Text(
                            text = stringResource(
                                id = R.string.settings_import_success,
                                result.importedCount,
                            ),
                        )
                    }
                    is ImportResult.Error -> {
                        Text(
                            text = stringResource(
                                id = R.string.settings_import_error,
                                result.message,
                            ),
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = viewModel::onImportResultDismissed) {
                    Text(text = stringResource(id = R.string.action_back))
                }
            },
        )
    }

    state.taskSetDialog?.let { dialog ->
        AlertDialog(
            onDismissRequest = viewModel::onTaskSetDialogDismiss,
            title = { Text(text = stringResource(id = R.string.settings_task_sets_add_dialog_title)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = dialog.name,
                        onValueChange = viewModel::onTaskSetDialogNameChange,
                        label = { Text(text = stringResource(id = R.string.settings_task_sets_name_label)) },
                        isError = dialog.isNameError,
                        supportingText = if (dialog.isNameError) {
                            { Text(text = stringResource(id = R.string.settings_task_sets_name_error)) }
                        } else null,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            },
            confirmButton = {
                Button(onClick = viewModel::onTaskSetDialogSaveClick) {
                    Text(text = stringResource(id = R.string.action_save))
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::onTaskSetDialogDismiss) {
                    Text(text = stringResource(id = R.string.action_cancel))
                }
            },
        )
    }
}

@Composable
private fun SoundThemeRow(
    theme: SpinSoundTheme,
    selected: SpinSoundTheme,
    onSelect: (SpinSoundTheme) -> Unit,
    label: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        RadioButton(
            selected = theme == selected,
            onClick = { onSelect(theme) },
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun ConfettiStyleRow(
    style: ConfettiStyle,
    selected: ConfettiStyle,
    onSelect: (ConfettiStyle) -> Unit,
    label: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        RadioButton(
            selected = style == selected,
            onClick = { onSelect(style) },
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun TaskSetRow(
    taskSet: TaskSet,
    isSelected: Boolean,
    onSelect: (TaskSet) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        RadioButton(
            selected = isSelected,
            onClick = { onSelect(taskSet) },
        )
        Text(
            text = taskSet.name,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}



