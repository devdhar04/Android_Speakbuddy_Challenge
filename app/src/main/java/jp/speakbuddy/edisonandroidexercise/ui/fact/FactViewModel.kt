package jp.speakbuddy.edisonandroidexercise.ui.fact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.speakbuddy.edisonandroidexercise.network.model.FactResponse
import jp.speakbuddy.edisonandroidexercise.repository.CatFactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FactViewModel @Inject constructor(
    private val catFactRepository: CatFactRepository
) : ViewModel() {

    private val _catFactResult = MutableStateFlow<Result<FactResponse?>>(Result.success(null)) // Allow null FactResponse
    val catFactResult: StateFlow<Result<FactResponse?>> = _catFactResult.asStateFlow()

    init {
        viewModelScope.launch {
            _catFactResult.value = runCatching {
                val savedFact = catFactRepository.getSavedCatFact()
                savedFact
            }
        }
    }

    fun fetchCatFact() {
        viewModelScope.launch {
            _catFactResult.value = kotlin.runCatching {
                catFactRepository.getCatFact()
            }
        }
    }
}
