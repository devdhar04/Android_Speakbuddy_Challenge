package jp.speakbuddy.edisonandroidexercise.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import jp.speakbuddy.edisonandroidexercise.utils.Result
import retrofit2.HttpException
import java.io.IOException

 suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
    return withContext(Dispatchers.IO) {
        try {
            val response = apiCall.invoke()
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error: ${response.code()} - ${response.message()}")
            }
        } catch (e: HttpException) {
            Result.Error("Network error: ${e.message()}")
        } catch (e: IOException) {
            Result.Error("Please check your internet connection and try again.")
        } catch (e: Exception) {
            Result.Error("Something went wrong: ${e.message}")
        }
    }
}