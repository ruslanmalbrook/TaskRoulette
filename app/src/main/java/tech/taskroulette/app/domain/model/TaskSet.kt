package tech.taskroulette.app.domain.model

data class TaskSet(
    val id: String,
    val name: String,
) {
    companion object {
        const val DEFAULT_ID: String = "default"
    }
}



