package jp.speakbuddy.edisonandroidexercise.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import jp.speakbuddy.edisonandroidexercise.BuildConfig
import jp.speakbuddy.edisonandroidexercise.R
import jp.speakbuddy.edisonandroidexercise.ui.FactScreenState
import jp.speakbuddy.edisonandroidexercise.ui.fact.FactScreenContent
import jp.speakbuddy.edisonandroidexercise.ui.theme.EdisonAndroidExerciseTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Preview
@Composable
private fun FactScreenPreview() {
    val _uiState = MutableStateFlow(
        FactScreenState.Success(
            fact = stringResource(R.string.cat_facts),
            factLength = 120,
            showMultipleCats = true,
            imageUrl = BuildConfig.CAT_URL
        )
    )
    val uiState: StateFlow<FactScreenState> = _uiState.asStateFlow()

    EdisonAndroidExerciseTheme {
        FactScreenContent(
            uiState = uiState.collectAsState().value,
            onRefresh = { }
        )
    }
}