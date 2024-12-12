package jp.speakbuddy.edisonandroidexercise.ui

data class FactScreenState(
    val fact: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val showMultipleCats: Boolean = false,
    val factLength: Int? = null,
    val imageUrl: String? = null
)