package jp.speakbuddy.edisonandroidexercise.ui.fact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.speakbuddy.edisonandroidexercise.storage.repository.CatFactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FactViewModel @Inject constructor(
    private val catFactRepository: CatFactRepository
) : ViewModel() {

    private val _catFactResult = MutableStateFlow(Result.success(""))
    val catFactResult: StateFlow<Result<String>> = _catFactResult.asStateFlow()

    init {
        viewModelScope.launch {
            _catFactResult.value = kotlin.runCatching {
                catFactRepository.getSavedCatFact().orEmpty()
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
