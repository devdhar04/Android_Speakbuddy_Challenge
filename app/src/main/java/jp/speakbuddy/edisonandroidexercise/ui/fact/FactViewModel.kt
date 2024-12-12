package jp.speakbuddy.edisonandroidexercise.ui.fact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.speakbuddy.edisonandroidexercise.BuildConfig
import jp.speakbuddy.edisonandroidexercise.repository.CatFactRepository
import jp.speakbuddy.edisonandroidexercise.repository.Result
import jp.speakbuddy.edisonandroidexercise.ui.FactScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FactViewModel @Inject constructor(
    private val catFactRepository: CatFactRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<FactScreenState>(FactScreenState.Loading)
    val uiState: StateFlow<FactScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            fetchSavedCatFact()
        }
    }

    private suspend fun fetchSavedCatFact() {
        _uiState.value = FactScreenState.Loading

        val savedFact = catFactRepository.getSavedCatFact()
        if (savedFact != null) {
            _uiState.value = FactScreenState.Success(
                fact = savedFact.fact,
                showMultipleCats = savedFact.fact.contains("cats", ignoreCase = true),
                factLength = savedFact.length,
                imageUrl = BuildConfig.CAT_URL
            )
        } else {
            fetchCatFact()
        }
    }

    fun fetchCatFact() {
        viewModelScope.launch {
            _uiState.value = FactScreenState.Loading
            when (val result = catFactRepository.getCatFact()) {
                is Result.Success -> {
                    val factResponse = result.data
                    _uiState.value = FactScreenState.Success(
                        fact = factResponse.fact,
                        showMultipleCats = factResponse.fact.contains("cats", ignoreCase = true),
                        factLength = factResponse.length,
                        imageUrl = BuildConfig.CAT_URL,
                        showFactLength = factResponse.length > 100
                    )
                }
                is Result.Error -> {
                    _uiState.value = FactScreenState.Error(
                        errorMessage = result.message ?: "An unexpected error occurred",
                        cause = Exception()
                    )
                }
            }
        }
    }
}


