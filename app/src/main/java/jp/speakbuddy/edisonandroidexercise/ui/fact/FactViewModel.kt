package jp.speakbuddy.edisonandroidexercise.ui.fact

import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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

    internal val _uiState = MutableStateFlow(FactScreenState(isLoading = true)) // Initial state
    val uiState: StateFlow<FactScreenState> = _uiState.asStateFlow()

    init {
        fetchCatFact() // Fetch initial fact
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
                        imageUrl = ""
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
