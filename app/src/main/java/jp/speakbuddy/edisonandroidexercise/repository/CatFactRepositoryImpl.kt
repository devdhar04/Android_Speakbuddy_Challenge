package jp.speakbuddy.edisonandroidexercise.repository


import jp.speakbuddy.edisonandroidexercise.network.FactService
import jp.speakbuddy.edisonandroidexercise.network.model.FactResponse
import jp.speakbuddy.edisonandroidexercise.storage.dao.CatFactDao
import jp.speakbuddy.edisonandroidexercise.storage.entity.CatFactEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CatFactRepositoryImpl @Inject constructor(
    private val catFactApi: FactService,
    private val catFactDao: CatFactDao
) : CatFactRepository {

    override suspend fun getCatFact(): FactResponse {
        return withContext(Dispatchers.IO) {
            val response = catFactApi.getFact()
            val factResponse = response.body()
            if (response.isSuccessful && factResponse != null) {
                catFactDao.insertCatFact(CatFactEntity(fact = factResponse.fact, length = factResponse.length))
                factResponse
            } else {
                throw Exception("Failed to fetch cat fact")
            }
        }
    }

    override suspend fun getSavedCatFact(): FactResponse? {
        return withContext(Dispatchers.IO) {
            val entity  = catFactDao.getLatestCatFact().firstOrNull()
            if (entity != null) {
                mapEntityToResponse(entity)
            } else {
                null
            }
        }
    }

    private fun mapEntityToResponse(entity: CatFactEntity): FactResponse {
        return FactResponse(fact = entity.fact, length = entity.length)
    }
}