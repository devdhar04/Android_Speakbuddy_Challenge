package jp.speakbuddy.edisonandroidexercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import jp.speakbuddy.edisonandroidexercise.ui.fact.FactScreen
import jp.speakbuddy.edisonandroidexercise.ui.theme.EdisonAndroidExerciseTheme
import jp.speakbuddy.edisonandroidexercise.viewmodel.FactViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: FactViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EdisonAndroidExerciseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FactScreen(viewModel = viewModel)
                }
            }
        }
    }
}