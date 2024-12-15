package jp.speakbuddy.edisonandroidexercise.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jp.speakbuddy.edisonandroidexercise.network.ApiClient
import jp.speakbuddy.edisonandroidexercise.network.FactService
import jp.speakbuddy.edisonandroidexercise.repository.CatFactRepository
import jp.speakbuddy.edisonandroidexercise.repository.CatFactRepositoryImpl
import jp.speakbuddy.edisonandroidexercise.storage.CatFactDatabase
import jp.speakbuddy.edisonandroidexercise.storage.dao.CatFactDao
import jp.speakbuddy.edisonandroidexercise.storage.utils.Constants
import jp.speakbuddy.edisonandroidexercise.utils.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Modules {

    @Provides
    @Singleton
    fun provideRetrofit(): ApiClient = ApiClient()

    @Provides
    @Singleton
    fun provideFactService(retrofit: ApiClient): FactService = retrofit.provide()

    @Provides
    @Singleton
    fun provideCatFactDatabase(@ApplicationContext context: Context): CatFactDatabase {
        return Room.databaseBuilder(
            context,
            CatFactDatabase::class.java,
            Constants.CAT_FACT_DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideCatFactDao(database: CatFactDatabase): CatFactDao {
        return database.catFactDao()
    }

    @Provides
    @Singleton
    fun provideCatFactRepository(
        catFactApi: FactService,
        catFactDao: CatFactDao,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CatFactRepository {
        return CatFactRepositoryImpl(
            catFactApi, catFactDao,
            ioDispatcher
        )
    }
}