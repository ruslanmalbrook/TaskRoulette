package tech.taskroulette.app.data.mapper

import tech.taskroulette.app.data.local.db.entity.TaskEntity
import tech.taskroulette.app.domain.model.Task

fun TaskEntity.toDomain(): Task = Task(
    id = id,
    title = title,
    colorArgb = colorArgb,
    weight = weight,
    taskSetId = taskSetId,
)

fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    title = title,
    colorArgb = colorArgb,
    weight = weight,
    taskSetId = taskSetId,
)


