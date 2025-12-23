package tech.taskroulette.app.domain.usecase.spin

import tech.taskroulette.app.domain.model.Task

data class SpinPlan(
    val selectedTask: Task,
    val targetRotationDegrees: Float,
    val durationMillis: Int,
    val tickStepDegrees: Float,
)


