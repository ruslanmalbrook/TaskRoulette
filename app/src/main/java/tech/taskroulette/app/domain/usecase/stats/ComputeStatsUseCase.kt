package tech.taskroulette.app.domain.usecase.stats

import javax.inject.Inject
import tech.taskroulette.app.domain.model.GameSession

class ComputeStatsUseCase @Inject constructor() {
    fun compute(sessions: List<GameSession>): Stats {
        if (sessions.isEmpty()) return Stats(totalSpins = 0, mostFrequentTaskTitle = null)

        val counts: MutableMap<String, CountAndTitle> = LinkedHashMap()
        sessions.forEach { session ->
            val selected = session.tasksSnapshot.firstOrNull { it.id == session.selectedSnapshotTaskId } ?: return@forEach
            val key = selected.originalTaskId ?: selected.title
            val current = counts[key]
            if (current == null) {
                counts[key] = CountAndTitle(count = 1, title = selected.title)
            } else {
                counts[key] = current.copy(count = current.count + 1)
            }
        }

        val best = counts.entries
            .sortedWith(
                compareByDescending<Map.Entry<String, CountAndTitle>> { it.value.count }
                    .thenBy { it.key },
            )
            .firstOrNull()

        return Stats(
            totalSpins = sessions.size,
            mostFrequentTaskTitle = best?.value?.title,
        )
    }

    private data class CountAndTitle(
        val count: Int,
        val title: String,
    )
}


