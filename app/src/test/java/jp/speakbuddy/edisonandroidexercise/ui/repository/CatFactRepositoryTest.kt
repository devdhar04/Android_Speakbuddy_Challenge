package jp.speakbuddy.edisonandroidexercise.ui.repository

import android.accounts.NetworkErrorException
import jp.speakbuddy.edisonandroidexercise.network.FactService
import jp.speakbuddy.edisonandroidexercise.network.model.FactResponse
import jp.speakbuddy.edisonandroidexercise.repository.CatFactRepositoryImpl
import jp.speakbuddy.edisonandroidexercise.storage.dao.CatFactDao
import jp.speakbuddy.edisonandroidexercise.storage.entity.CatFactEntity
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException
import retrofit2.Response
import jp.speakbuddy.edisonandroidexercise.utils.Result
import okio.IOException

@RunWith(MockitoJUnitRunner::class)
class CatFactRepositoryTest {

    @Mock
    private lateinit var catFactApi: FactService

    @Mock
    private lateinit var catFactDao: CatFactDao

    private lateinit var repository: CatFactRepositoryImpl

    @Before
    fun setup() {
        repository = CatFactRepositoryImpl(catFactApi, catFactDao)
    }

    @Test
    fun `getCatFact success`() = runTest {
        val factResponse = FactResponse("This is a cat fact.", 15)
        val response = Response.success(factResponse)
        Mockito.`when`(catFactApi.getFact()).thenReturn(response)

        val result = repository.getCatFact()

        Assert.assertTrue(result is Result)
        Assert.assertEquals(factResponse, (result as Result.Success).data)

        Mockito.verify(catFactDao).insertCatFact(
            CatFactEntity(fact = factResponse.fact, length = factResponse.length)
        )
    }

    @Test
    fun `getCatFact api error`() = runTest {
        val exception = HttpException(Response.error<FactResponse>(500, ResponseBody.create(null, "")))
        Mockito.`when`(catFactApi.getFact()).thenThrow(exception)

        repository.getCatFact()
    }

    @Test
    fun `testUnexpectedError`() = runTest {
        // Mock the API to throw a generic exception
        Mockito.`when`(catFactApi.getFact()).thenThrow(RuntimeException("Unexpected error"))

        val result = repository.getCatFact()

        Assert.assertTrue(result is Result.Error)
        Assert.assertEquals("Something went wrong: Unexpected error", (result as Result.Error).message)
    }

    @Test
    fun `getSavedCatFact success`() = runTest {
        val factEntity = CatFactEntity(fact = "This is a saved cat fact.", length = 20)
        Mockito.`when`(catFactDao.getLatestCatFact()).thenReturn(flowOf(factEntity))

        val result = repository.getSavedCatFact()

        Assert.assertEquals(FactResponse(fact = factEntity.fact, length = factEntity.length), result)
    }

    @Test
    fun `getSavedCatFact null`() = runTest {
        Mockito.`when`(catFactDao.getLatestCatFact()).thenReturn(flowOf(null))

        val result = repository.getSavedCatFact()

        Assert.assertNull(result)
    }
}