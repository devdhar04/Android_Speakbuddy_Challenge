package jp.speakbuddy.edisonandroidexercise.network

import jp.speakbuddy.edisonandroidexercise.network.model.FactResponse
import retrofit2.Response
import retrofit2.http.GET

interface FactService {
    @GET("fact")
    suspend fun getFact(): Response<FactResponse>
}

