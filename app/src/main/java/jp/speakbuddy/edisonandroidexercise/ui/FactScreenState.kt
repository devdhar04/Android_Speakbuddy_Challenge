package jp.speakbuddy.edisonandroidexercise.ui

import jp.speakbuddy.edisonandroidexercise.BuildConfig

/**
 * Represents the different states of the Fact Screen in the UI.
 */
sealed class FactScreenState {

    /**
     * Represents the loading state of the UI.
     */
    object Loading : FactScreenState()

    /**
     * Represents the state when a fact is successfully loaded.
     *
     * @param fact The cat fact text to display.
     * @param showMultipleCats Indicates if the fact mentions multiple cats.
     * @param factLength The length of the fact text.
     * @param imageUrl The URL of the image to display alongside the fact.
     */
    data class Success(
        val fact: String,
        val showMultipleCats: Boolean = false,
        val factLength: Int = 0,
        val imageUrl: String? = BuildConfig.CAT_URL,
        val showFactLength: Boolean = false
    ) : FactScreenState()

    /**
     * Represents the state when an error occurs.
     *
     * @param errorMessage The error message to display.
     * @param cause An optional throwable cause for debugging.
     */
    data class Error(
        val errorMessage: String
    ) : FactScreenState()
}
