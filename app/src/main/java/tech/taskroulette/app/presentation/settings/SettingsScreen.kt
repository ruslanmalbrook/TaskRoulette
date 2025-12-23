package tech.taskroulette.app.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tech.taskroulette.app.R
import tech.taskroulette.app.domain.model.ConfettiStyle
import tech.taskroulette.app.domain.model.SpinSoundTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

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
        }
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


