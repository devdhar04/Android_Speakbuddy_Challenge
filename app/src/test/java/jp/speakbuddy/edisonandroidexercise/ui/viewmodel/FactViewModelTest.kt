package jp.speakbuddy.edisonandroidexercise.ui.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import jp.speakbuddy.edisonandroidexercise.BuildConfig
import jp.speakbuddy.edisonandroidexercise.network.model.FactResponse
import jp.speakbuddy.edisonandroidexercise.repository.CatFactRepository
import jp.speakbuddy.edisonandroidexercise.ui.FactScreenState
import jp.speakbuddy.edisonandroidexercise.utils.Result
import jp.speakbuddy.edisonandroidexercise.viewmodel.FactViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class FactViewModelTest {

    @Mock
    private lateinit var catFactRepository: CatFactRepository

    @Mock
    private lateinit var context: Context

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: FactViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = FactViewModel(catFactRepository, context = context)
    }

    @Test
    fun `fetchSavedCatFact success`() = runTest {
        val savedFact = FactResponse("This is a saved cat fact.", 15)
        Mockito.`when`(catFactRepository.getSavedCatFact()).thenReturn(savedFact)

        viewModel.fetchSavedCatFact()

        Assert.assertEquals(
            FactScreenState.Success(
                fact = savedFact.fact,
                showMultipleCats = savedFact.fact.contains("cats", ignoreCase = true),
                factLength = savedFact.length,
                imageUrl = BuildConfig.CAT_URL,
                showFactLength = savedFact.length > 100
            ),
            viewModel.uiState.value
        )
    }

    @Test
    fun `fetchSavedCatFact null then fetchCatFact`() = runTest {
        Mockito.`when`(catFactRepository.getSavedCatFact()).thenReturn(null)
        val factResponse = FactResponse("This is a cat fact.", 15)
        Mockito.`when`(catFactRepository.getCatFact()).thenReturn(Result.Success(factResponse))

        viewModel.fetchSavedCatFact()
        advanceUntilIdle()
        Assert.assertEquals(
            FactScreenState.Success(
                fact = factResponse.fact,
                showMultipleCats = factResponse.fact.contains("cats", ignoreCase = true),
                factLength = factResponse.length,
                imageUrl = BuildConfig.CAT_URL,
                showFactLength = factResponse.length > 100
            ),
            viewModel.uiState.value
        )
    }

    @Test
    fun `fetchCatFact success`() = runTest {
        val factResponse = FactResponse("This is a cat fact.", 15)
        Mockito.`when`(catFactRepository.getCatFact()).thenReturn(Result.Success(factResponse))

        viewModel.fetchCatFact()
        advanceUntilIdle()
        Assert.assertEquals(
            FactScreenState.Success(
                fact = factResponse.fact,
                showMultipleCats = factResponse.fact.contains("cats", ignoreCase = true),
                factLength = factResponse.length,
                imageUrl = BuildConfig.CAT_URL,
                showFactLength = factResponse.length > 100
            ),
            viewModel.uiState.value
        )
    }

    @Test
    fun `fetchCatFact error`() = runTest {
        val exception = Exception()
        whenever(catFactRepository.getCatFact()).thenReturn(Result.Error.UnknownError)
        whenever(context.getString(any())).thenReturn("Network error")

        viewModel.fetchCatFact()
        val uiState = viewModel.uiState.value
        advanceUntilIdle()
        Assert.assertEquals(
            FactScreenState.Error(
                errorMessage = "Network error",
            ),
            viewModel.uiState.value
        )
    }
}
