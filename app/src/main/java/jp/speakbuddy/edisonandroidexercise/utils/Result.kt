package jp.speakbuddy.edisonandroidexercise.utils

/**
 * Represents the result of an operation, which can be either success or failure.
 * @param T The type of data returned in case of success.
 */
sealed class Result<out T> {
    /**
     * Represents a successful result with the data.
     *
     * @param data The data returned by the operation.
     */
    data class Success<out T>(val data: T) : Result<T>()

    /**
     * Represents a failed result with an error.
     */
    sealed class Error() : Result<Nothing>() {
        /**
         * Represents a network error.
         */
        data object NetworkError : Error()

        /**
         * Represents an API error.
         */
        data object ApiError : Error()

        /**
         * Represents an unknown error.
         */
        data object UnknownError : Error()
    }
}