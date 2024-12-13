package jp.speakbuddy.edisonandroidexercise.repository

import jp.speakbuddy.edisonandroidexercise.network.model.FactResponse
import jp.speakbuddy.edisonandroidexercise.utils.Result

interface CatFactRepository {
    suspend fun getCatFact(): Result<FactResponse>
    suspend fun getSavedCatFact(): FactResponse?
}