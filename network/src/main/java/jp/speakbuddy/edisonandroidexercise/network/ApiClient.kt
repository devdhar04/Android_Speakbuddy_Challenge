package jp.speakbuddy.edisonandroidexercise.network

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

class ApiClient {

    companion object {
        const val BASE_URL = "https://catfact.ninja/"
    }

    fun provide(): FactService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FactService::class.java)
}