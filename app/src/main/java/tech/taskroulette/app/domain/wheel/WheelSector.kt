package tech.taskroulette.app.domain.wheel

import tech.taskroulette.app.domain.model.Task

data class WheelSector(
    val task: Task,
    val startAngleDegrees: Double,
    val sweepAngleDegrees: Double,
)


