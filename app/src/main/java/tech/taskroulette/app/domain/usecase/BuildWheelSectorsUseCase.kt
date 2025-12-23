package tech.taskroulette.app.domain.usecase

import javax.inject.Inject
import tech.taskroulette.app.domain.model.Task
import tech.taskroulette.app.domain.wheel.WheelSector

class BuildWheelSectorsUseCase @Inject constructor() {

    fun build(tasks: List<Task>): List<WheelSector> {
        if (tasks.isEmpty()) return emptyList()
        if (tasks.size == 1) {
            return listOf(
                WheelSector(
                    task = tasks.first(),
                    startAngleDegrees = 0.0,
                    sweepAngleDegrees = 360.0,
                ),
            )
        }

        val normalizedWeights = tasks.map { it.weight.coerceAtLeast(1).toDouble() }
        val totalWeight = normalizedWeights.sum()
        if (totalWeight <= 0.0) {
            // Reason: defensive fallback for corrupted data; equal sectors.
            val equal = 360.0 / tasks.size.toDouble()
            return tasks.mapIndexed { index, task ->
                WheelSector(
                    task = task,
                    startAngleDegrees = equal * index,
                    sweepAngleDegrees = equal,
                )
            }
        }

        val rawSweeps = normalizedWeights.map { weight -> 360.0 * weight / totalWeight }
        val sweeps = rawSweeps.toMutableList()
        val sumExceptLast = rawSweeps.dropLast(1).sum()
        sweeps[sweeps.lastIndex] = 360.0 - sumExceptLast

        if (sweeps.last() < 0.0) {
            // Reason: guard against tiny floating rounding overshoot (sumExceptLast > 360).
            val overshoot = -sweeps.last()
            sweeps[sweeps.lastIndex] = 0.0
            val idxToReduce = sweeps.indices.maxBy { sweeps[it] }
            sweeps[idxToReduce] = (sweeps[idxToReduce] - overshoot).coerceAtLeast(0.0)
        }

        val result = ArrayList<WheelSector>(tasks.size)
        var start = 0.0
        for (i in tasks.indices) {
            val sweep = sweeps[i]
            result += WheelSector(
                task = tasks[i],
                startAngleDegrees = start,
                sweepAngleDegrees = sweep,
            )
            start += sweep
        }
        return result
    }
}


