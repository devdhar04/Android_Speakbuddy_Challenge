package jp.speakbuddy.edisonandroidexercise.utils

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    sealed class Error() : Result<Nothing>() {
        data object NetworkError : Error()
        data object ApiError : Error()
        data object UnknownError : Error()
    }
}