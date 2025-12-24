package tech.taskroulette.app.data.mapper

import tech.taskroulette.app.data.local.db.entity.TaskSetEntity
import tech.taskroulette.app.domain.model.TaskSet

fun TaskSetEntity.toDomain(): TaskSet = TaskSet(
    id = id,
    name = name,
)

fun TaskSet.toEntity(): TaskSetEntity = TaskSetEntity(
    id = id,
    name = name,
)



