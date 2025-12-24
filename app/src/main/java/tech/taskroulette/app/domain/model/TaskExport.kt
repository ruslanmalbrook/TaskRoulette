package tech.taskroulette.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TaskExport(
    val version: Int = 1,
    val taskSetName: String,
    val tasks: List<TaskExportItem>,
)

@Serializable
data class TaskExportItem(
    val title: String,
    val colorArgb: Int,
    val weight: Int,
)

