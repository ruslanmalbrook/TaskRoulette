package tech.taskroulette.app.data.local.db.model

import androidx.room.Embedded
import androidx.room.Relation
import tech.taskroulette.app.data.local.db.entity.GameSessionEntity
import tech.taskroulette.app.data.local.db.entity.SessionTaskEntity

data class GameSessionWithTasks(
    @Embedded
    val session: GameSessionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "sessionId",
    )
    val tasks: List<SessionTaskEntity>,
)


