package uk.ac.tees.mad.journalify

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import uk.ac.tees.mad.journalify.data.session.SessionManager
import uk.ac.tees.mad.journalify.navigation.NavGraph
import uk.ac.tees.mad.journalify.ui.theme.JournalifyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    @Inject
    lateinit var session: SessionManager

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val dark by session.themeDark.collectAsState(initial = false)
            JournalifyTheme(darkTheme = dark) {
                NavGraph(modifier = Modifier)
            }
        }
    }
}
