package jp.speakbuddy.edisonandroidexercise.network


import jp.speakbuddy.edisonandroidexercise.storage.dao.CatFactDao
import jp.speakbuddy.edisonandroidexercise.storage.entity.CatFactEntity
import jp.speakbuddy.edisonandroidexercise.storage.repository.CatFactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CatFactRepositoryImpl @Inject constructor(
    private val catFactApi: FactService,
    private val catFactDao: CatFactDao
) : CatFactRepository {

    override suspend fun getCatFact(): String {
        return withContext(Dispatchers.IO) {
            val latestCatFact = catFactDao.getLatestCatFact().firstOrNull()
            if (latestCatFact != null) {
                latestCatFact.fact
            } else {
                val newCatFact = catFactApi.getFact().fact
                catFactDao.insertCatFact(CatFactEntity(fact = newCatFact))
                newCatFact
            }
        }
    }
}