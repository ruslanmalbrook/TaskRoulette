package tech.taskroulette.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import tech.taskroulette.app.domain.IdGenerator
import tech.taskroulette.app.presentation.TaskRouletteApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var idGenerator: IdGenerator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        idGenerator.newId() // Reason: ensure DI is wired; actual usage comes in later slices
        setContent {
            TaskRouletteApp()
        }
    }
}