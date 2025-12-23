package tech.taskroulette.app.domain

import java.util.UUID
import javax.inject.Inject

class UuidIdGenerator @Inject constructor() : IdGenerator {
    override fun newId(): String = UUID.randomUUID().toString()
}


