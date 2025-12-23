package tech.taskroulette.app.domain.random

class FakeRandomProvider(
    private val ints: ArrayDeque<Int> = ArrayDeque(),
    private val longs: ArrayDeque<Long> = ArrayDeque(),
) : RandomProvider {

    override fun nextInt(untilExclusive: Int): Int {
        if (untilExclusive <= 0) throw IllegalArgumentException("untilExclusive must be > 0")
        val value = (ints.removeFirstOrNull() ?: 0)
        return value.floorMod(untilExclusive)
    }

    override fun nextLong(untilExclusive: Long): Long {
        if (untilExclusive <= 0L) throw IllegalArgumentException("untilExclusive must be > 0")
        val value = (longs.removeFirstOrNull() ?: 0L)
        return value.floorMod(untilExclusive)
    }

    private fun Int.floorMod(mod: Int): Int = ((this % mod) + mod) % mod

    private fun Long.floorMod(mod: Long): Long = ((this % mod) + mod) % mod
}


