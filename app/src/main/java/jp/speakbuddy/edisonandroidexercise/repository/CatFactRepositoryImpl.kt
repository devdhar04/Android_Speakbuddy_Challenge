package jp.speakbuddy.edisonandroidexercise.repository

import jp.speakbuddy.edisonandroidexercise.network.FactService
import jp.speakbuddy.edisonandroidexercise.network.model.FactResponse
import jp.speakbuddy.edisonandroidexercise.storage.dao.CatFactDao
import jp.speakbuddy.edisonandroidexercise.storage.entity.CatFactEntity
import jp.speakbuddy.edisonandroidexercise.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CatFactRepositoryImpl @Inject constructor(
    private val catFactApi: FactService,
    private val catFactDao: CatFactDao
) : CatFactRepository {

    override suspend fun getCatFact(): Result<FactResponse> {
        return safeApiCall {
            val response = catFactApi.getFact()
            if (response.isSuccessful) {
                val factResponse = response.body()
                factResponse?.let {
                    catFactDao.insertCatFact(CatFactEntity(fact = it.fact, length = it.length))
                }
            }
            response
        }
    }

    override suspend fun getSavedCatFact(): FactResponse? {
        return withContext(Dispatchers.IO) {
            catFactDao.getLatestCatFact().firstOrNull()?.let { entity ->
                mapEntityToResponse(entity)
            }
        }
    }

    private fun mapEntityToResponse(entity: CatFactEntity): FactResponse {
        return FactResponse(fact = entity.fact, length = entity.length)
    }
}
