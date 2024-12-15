package jp.speakbuddy.edisonandroidexercise.repository

import jp.speakbuddy.edisonandroidexercise.utils.Config.Companion.RETRY_COUNT
import jp.speakbuddy.edisonandroidexercise.utils.Config.Companion.RETRY_DELAY
import jp.speakbuddy.edisonandroidexercise.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

suspend fun <T> safeApiCall(
    apiCall: suspend () -> Response<T>,
    retries: Int = RETRY_COUNT,
    delayMillis: Long = RETRY_DELAY
): Result<T> {
    return withContext(Dispatchers.IO) {
        var currentAttempt = 0
        var lastError: Exception? = null

        while (currentAttempt <= retries) {
            lastError = try {
                val response = apiCall.invoke()
                if (response.isSuccessful && response.body() != null) {
                    return@withContext Result.Success(response.body()!!)
                } else {
                    return@withContext Result.Error.ApiError
                }
            } catch (e: HttpException) {
                e
            } catch (e: IOException) {
                e
            } catch (e: Exception) {
                e
            }

            currentAttempt++
            if (currentAttempt <= retries) {
                delay(delayMillis)
            }
        }

        val error = when (lastError) {
            is HttpException -> Result.Error.NetworkError
            is IOException -> Result.Error.ApiError
            else -> Result.Error.UnknownError
        }
        error
    }
}
