package tech.taskroulette.app.domain.usecase

import javax.inject.Inject
import tech.taskroulette.app.domain.model.Task
import tech.taskroulette.app.domain.random.RandomProvider

class SelectTaskByWeightUseCase @Inject constructor(
    private val randomProvider: RandomProvider,
) {
    fun select(tasks: List<Task>): Task? {
        if (tasks.isEmpty()) return null

        val normalizedWeights = tasks.map { it.weight.coerceAtLeast(1).toLong() }
        val totalWeight = normalizedWeights.sum()
        if (totalWeight <= 0L) return tasks.first() // Reason: defensive fallback for corrupted data.

        val random = randomProvider.nextLong(totalWeight)
        var acc = 0L
        for (i in tasks.indices) {
            acc += normalizedWeights[i]
            if (random < acc) return tasks[i]
        }

        return tasks.last() // Reason: avoid null due to edge-case rounding/overflow.
    }
}


