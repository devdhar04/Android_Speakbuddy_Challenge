package jp.speakbuddy.edisonandroidexercise.utils

import android.content.Context
import jp.speakbuddy.edisonandroidexercise.R

fun Context.getErrorMessage(result: Result.Error): String {
    return when (result) {
        is Result.Error.NetworkError -> getString(R.string.network_error)
        is Result.Error.ApiError -> getString(R.string.api_error)
        is Result.Error.UnknownError -> getString(R.string.unknown_error)
    }
}