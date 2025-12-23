package tech.taskroulette.app.domain.random

import javax.inject.Inject
import kotlin.random.Random

class DefaultRandomProvider @Inject constructor() : RandomProvider {
    private val random: Random = Random.Default

    override fun nextInt(untilExclusive: Int): Int = random.nextInt(untilExclusive)

    override fun nextLong(untilExclusive: Long): Long = random.nextLong(untilExclusive)
}


