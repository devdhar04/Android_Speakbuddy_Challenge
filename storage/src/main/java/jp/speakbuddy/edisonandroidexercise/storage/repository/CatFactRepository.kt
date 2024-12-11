package jp.speakbuddy.edisonandroidexercise.storage.repository

interface CatFactRepository {
    suspend fun getCatFact(): String
    suspend fun getSavedCatFact(): String?
}