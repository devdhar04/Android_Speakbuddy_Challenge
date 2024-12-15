package jp.speakbuddy.edisonandroidexercise.viewmodel

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jp.speakbuddy.edisonandroidexercise.repository.CatFactRepository
import jp.speakbuddy.edisonandroidexercise.ui.FactScreenState
import jp.speakbuddy.edisonandroidexercise.utils.Config.Companion.FACT_LENGTH
import jp.speakbuddy.edisonandroidexercise.utils.Config.Companion.SEARCH_TEXT
import jp.speakbuddy.edisonandroidexercise.utils.Result
import jp.speakbuddy.edisonandroidexercise.utils.getErrorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FactViewModel @Inject constructor(
    private val catFactRepository: CatFactRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<FactScreenState>(FactScreenState.Loading)
    val uiState: StateFlow<FactScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            fetchSavedCatFact()
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun fetchSavedCatFact() {
        _uiState.value = FactScreenState.Loading

        val savedFact = catFactRepository.getSavedCatFact()
        savedFact?.let { (fact, length) ->
            _uiState.value = createSuccessState(fact, length)
        } ?: fetchCatFact()
    }

    fun fetchCatFact() {
        viewModelScope.launch {
            _uiState.value = FactScreenState.Loading
            when (val result = catFactRepository.getCatFact()) {
                is Result.Success -> {
                    val factResponse = result.data
                    _uiState.value = createSuccessState(factResponse.fact, factResponse.length)
                }

                is Result.Error -> {
                    _uiState.value = FactScreenState.Error(
                        errorMessage = context.getErrorMessage(result),
                    )
                }
            }
        }
    }

    private fun createSuccessState(fact: String, length: Int): FactScreenState.Success {
        return FactScreenState.Success(
            fact = fact,
            showMultipleCats = fact.contains(SEARCH_TEXT, ignoreCase = true),
            factLength = length,
            showFactLength = length > FACT_LENGTH
        )
    }
}


