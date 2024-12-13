package jp.speakbuddy.edisonandroidexercise.ui.fact

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import jp.speakbuddy.edisonandroidexercise.viewmodel.FactViewModel

@Composable
fun FactScreen(
    viewModel: FactViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    FactScreenContent(
        uiState = uiState,
        onRefresh = { viewModel.fetchCatFact() }
    )
}


