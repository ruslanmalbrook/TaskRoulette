package tech.taskroulette.benchmark

import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartupBenchmark {

    @get:Rule
    val benchmarkRule: MacrobenchmarkRule = MacrobenchmarkRule()

    @Test
    fun coldStartup(): Unit {
        benchmarkRule.measureRepeated(
            packageName = TARGET_PACKAGE,
            metrics = listOf(StartupTimingMetric()),
            iterations = 5,
            startupMode = StartupMode.COLD,
            setupBlock = {
                pressHome()
            },
        ) {
            startActivityAndWait()
        }
    }

    private companion object {
        private const val TARGET_PACKAGE: String = "tech.taskroulette.app"
    }
}


