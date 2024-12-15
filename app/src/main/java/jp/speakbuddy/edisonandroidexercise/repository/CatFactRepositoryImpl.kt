package jp.speakbuddy.edisonandroidexercise.repository

import jp.speakbuddy.edisonandroidexercise.network.FactService
import jp.speakbuddy.edisonandroidexercise.network.model.FactResponse
import jp.speakbuddy.edisonandroidexercise.storage.dao.CatFactDao
import jp.speakbuddy.edisonandroidexercise.storage.entity.CatFactEntity
import jp.speakbuddy.edisonandroidexercise.utils.Config.Companion.RETRY_COUNT
import jp.speakbuddy.edisonandroidexercise.utils.Config.Companion.RETRY_DELAY
import jp.speakbuddy.edisonandroidexercise.utils.IoDispatcher
import jp.speakbuddy.edisonandroidexercise.utils.Result
import jp.speakbuddy.edisonandroidexercise.utils.mapEntityToResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CatFactRepositoryImpl @Inject constructor(
    private val catFactApi: FactService,
    private val catFactDao: CatFactDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CatFactRepository {

    override suspend fun getCatFact(): Result<FactResponse> {
        return safeApiCall(
            apiCall = {
                val response = catFactApi.getFact()
                if (response.isSuccessful) {
                    val factResponse = response.body()
                    factResponse?.let {
                        catFactDao.insertCatFact(CatFactEntity(fact = it.fact, length = it.length))
                    }
                }
                response
            },
            retries = RETRY_COUNT,
            delayMillis = RETRY_DELAY
        )
    }

    override suspend fun getSavedCatFact(): FactResponse? {
        return withContext(ioDispatcher) {
            catFactDao.getLatestCatFact().firstOrNull()?.mapEntityToResponse()
        }
    }
}
