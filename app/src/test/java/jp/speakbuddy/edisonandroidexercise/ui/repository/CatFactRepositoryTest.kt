package jp.speakbuddy.edisonandroidexercise.ui.repository

import jp.speakbuddy.edisonandroidexercise.network.FactService
import jp.speakbuddy.edisonandroidexercise.network.model.FactResponse
import jp.speakbuddy.edisonandroidexercise.repository.CatFactRepositoryImpl
import jp.speakbuddy.edisonandroidexercise.storage.dao.CatFactDao
import jp.speakbuddy.edisonandroidexercise.storage.entity.CatFactEntity
import jp.speakbuddy.edisonandroidexercise.utils.Result
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

@RunWith(MockitoJUnitRunner::class)
class CatFactRepositoryTest {

    @Mock
    private lateinit var catFactApi: FactService

    @Mock
    private lateinit var catFactDao: CatFactDao

    private lateinit var repository: CatFactRepositoryImpl

    @Before
    fun setup() {
        repository = CatFactRepositoryImpl(catFactApi, catFactDao, ioDispatcher = kotlinx.coroutines.Dispatchers.IO)
    }

    @Test
    fun `getCatFact success`() = runTest {
        // Arrange: Mock the API response
        val factResponse = FactResponse("This is a cat fact.", 15)
        val apiResponse = Response.success(factResponse)
        Mockito.`when`(catFactApi.getFact()).thenReturn(apiResponse)

        // Act: Call the repository's getCatFact method
        val result = repository.getCatFact()

        // Assert: Verify the result is of type Result.Success
        Assert.assertTrue(result is Result.Success)
        Assert.assertEquals(factResponse, (result as Result.Success).data)


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
        Mockito.`when`(catFactApi.getFact()).thenThrow(RuntimeException("UnknownError"))

        val result = repository.getCatFact()

        Assert.assertTrue(result is Result.Error)
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