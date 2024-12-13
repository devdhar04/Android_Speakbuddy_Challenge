package jp.speakbuddy.edisonandroidexercise.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import jp.speakbuddy.edisonandroidexercise.utils.Result
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    apiCall: suspend () -> Response<T>,
    retries: Int = 3,
    delayMillis: Long = 1000L
): Result<T> {
    return withContext(Dispatchers.IO) {
        var currentAttempt = 0
        var lastError: Exception? = null

        while (currentAttempt <= retries) {
            try {
                val response = apiCall.invoke()
                if (response.isSuccessful && response.body() != null) {
                    return@withContext Result.Success(response.body()!!)
                } else {
                    return@withContext Result.Error("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: HttpException) {
                lastError = e
            } catch (e: IOException) {
                lastError = e
            } catch (e: Exception) {
                lastError = e
            }

            // Increment attempt counter and delay before retrying
            currentAttempt++
            if (currentAttempt <= retries) {
                delay(delayMillis)
            }
        }

        // If all retries fail, return the last error as a Result
        val errorMessage = when (lastError) {
            is HttpException -> "Network error: ${lastError.message()}"
            is IOException -> "Please check your internet connection and try again."
            else -> "Something went wrong: ${lastError?.message}"
        }
        Result.Error(errorMessage)
    }
}
