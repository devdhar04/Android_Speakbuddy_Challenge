package jp.speakbuddy.edisonandroidexercise.repository

import jp.speakbuddy.edisonandroidexercise.network.model.FactResponse

interface CatFactRepository {
    suspend fun getCatFact(): Result<FactResponse>
    suspend fun getSavedCatFact(): FactResponse?
}