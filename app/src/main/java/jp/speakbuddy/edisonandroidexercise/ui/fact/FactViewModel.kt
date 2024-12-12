package jp.speakbuddy.edisonandroidexercise.ui.fact

import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.speakbuddy.edisonandroidexercise.BuildConfig
import jp.speakbuddy.edisonandroidexercise.network.model.FactResponse
import jp.speakbuddy.edisonandroidexercise.repository.CatFactRepository
import jp.speakbuddy.edisonandroidexercise.ui.FactScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class FactViewModel @Inject constructor(
    private val catFactRepository: CatFactRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FactScreenState(isLoading = true)) // Initial state
    val uiState: StateFlow<FactScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val savedFact = catFactRepository.getSavedCatFact()
            if (savedFact != null) {
                _uiState.value = _uiState.value.copy(fact = savedFact.fact, isLoading = false, imageUrl = BuildConfig.CAT_URL)
            } else {
                fetchCatFact()
            }
        }
    }

    open fun fetchCatFact() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null) // Show loading
            try {
                val factResponse = catFactRepository.getCatFact()
                if (factResponse != null) {
                    _uiState.value = _uiState.value.copy(
                        fact = factResponse.fact,
                        isLoading = false,
                        showMultipleCats = factResponse.fact.contains("cats", ignoreCase = true),
                        factLength = factResponse.length,
                        imageUrl = BuildConfig.CAT_URL
                    )
                }
                else{
                    _uiState.value = _uiState.value.copy(isLoading = false, error = "No fact found")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message) // Show error
            }
        }
    }
}
