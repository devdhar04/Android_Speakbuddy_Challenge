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
            val response = catFactApi.getFact() // Call FactService
            if (response.isSuccessful) {
                val factResponse = response.body()
                val newCatFact = factResponse?.fact ?: "" // Handle null case
                catFactDao.insertCatFact(CatFactEntity(fact = newCatFact))
                newCatFact
            } else {
                // Handle error (e.g., throw an exception)
                throw Exception("Failed to fetch cat fact")
            }
        }
    }

    override suspend fun getSavedCatFact(): String? {
        return withContext(Dispatchers.IO) {
            catFactDao.getLatestCatFact().firstOrNull()?.fact
        }
    }
}