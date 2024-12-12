package jp.speakbuddy.edisonandroidexercise.ui.fact

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import jp.speakbuddy.edisonandroidexercise.ui.FactScreenState
import jp.speakbuddy.edisonandroidexercise.ui.theme.EdisonAndroidExerciseTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun FactScreen(
    viewModel: FactViewModel
) {
    val uiState by viewModel.uiState.collectAsState() // Collect UI state

    FactScreenContent(
        uiState = uiState,
        onRefresh = { viewModel.fetchCatFact() } // Pass refresh action
    )
}

@Preview
@Composable
private fun FactScreenPreview() {
    val _uiState = MutableStateFlow(FactScreenState(fact = "Test Fact", factLength = 100)) // Initial state
    val uiState: StateFlow<FactScreenState> = _uiState.asStateFlow()

    EdisonAndroidExerciseTheme {
        FactScreenContent(
            uiState = uiState.collectAsState().value,
            onRefresh = { } // Pass refresh action
        )
    }
}
