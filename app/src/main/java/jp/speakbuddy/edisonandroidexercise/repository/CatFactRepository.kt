package jp.speakbuddy.edisonandroidexercise.repository

import jp.speakbuddy.edisonandroidexercise.network.model.FactResponse

interface CatFactRepository {
    suspend fun getCatFact(): FactResponse?
    suspend fun getSavedCatFact(): FactResponse?
}