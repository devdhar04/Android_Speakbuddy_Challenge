package jp.speakbuddy.edisonandroidexercise.network

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

class ApiClient {

    fun provide(): FactService =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FactService::class.java)
}