package tech.taskroulette.app.domain.random

interface RandomProvider {
    fun nextInt(untilExclusive: Int): Int

    fun nextLong(untilExclusive: Long): Long
}


