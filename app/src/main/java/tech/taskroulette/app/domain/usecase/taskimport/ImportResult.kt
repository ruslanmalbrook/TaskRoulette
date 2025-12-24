package tech.taskroulette.app.domain.usecase.taskimport

sealed class ImportResult {
    data class Success(
        val taskSetId: String,
        val importedCount: Int,
    ) : ImportResult()

    data class Error(val message: String) : ImportResult()
}

