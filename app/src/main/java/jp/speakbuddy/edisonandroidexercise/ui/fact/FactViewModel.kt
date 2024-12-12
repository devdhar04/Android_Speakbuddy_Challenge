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

    private val _uiState = MutableStateFlow(FactScreenState(isLoading = true))
    val uiState: StateFlow<FactScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            fetchSavedCatFact()
        }
    }

    private suspend fun fetchSavedCatFact() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        val savedFact = catFactRepository.getSavedCatFact()
        if (savedFact != null) {
            _uiState.value = _uiState.value.copy(
                fact = savedFact.fact,
                isLoading = false,
                imageUrl = BuildConfig.CAT_URL
            )
        } else {
            fetchCatFact()
        }
    }

    fun fetchCatFact() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = catFactRepository.getCatFact()) {
                is Result.Success -> {
                    val factResponse = result.data
                    _uiState.value = _uiState.value.copy(
                        fact = factResponse.fact,
                        isLoading = false,
                        showMultipleCats = factResponse.fact.contains("cats", ignoreCase = true),
                        factLength = factResponse.length,
                        imageUrl = BuildConfig.CAT_URL
                    )
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }
}


