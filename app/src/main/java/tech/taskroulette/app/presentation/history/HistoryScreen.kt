package tech.taskroulette.app.presentation.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.DateFormat
import java.util.Date
import kotlinx.coroutines.flow.collectLatest
import tech.taskroulette.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    onNavigateToResult: (String) -> Unit,
    onNavigateToReplay: (String) -> Unit,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is HistoryEvent.NavigateToResult -> onNavigateToResult(event.sessionId)
                is HistoryEvent.NavigateToReplay -> onNavigateToReplay(event.sessionId)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.history_title)) },
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
            if (state.sessions.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.history_empty),
                    style = MaterialTheme.typography.bodyLarge,
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(
                        items = state.sessions,
                        key = { it.id },
                    ) { item ->
                        SessionRow(
                            item = item,
                            onClick = { viewModel.onSessionClick(item.id) },
                            onReplayClick = { viewModel.onReplayClick(item.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SessionRow(
    item: HistorySessionItem,
    onClick: () -> Unit,
    onReplayClick: () -> Unit,
) {
    val dateText = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
        .format(Date(item.createdAtEpochMillis))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.selectedTitle ?: stringResource(id = R.string.placeholder_dash),
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
            )
            Text(
                text = dateText,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        OutlinedButton(onClick = onReplayClick) {
            Text(text = stringResource(id = R.string.action_replay))
        }
    }
}


