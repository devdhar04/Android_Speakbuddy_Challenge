package jp.speakbuddy.edisonandroidexercise.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jp.speakbuddy.edisonandroidexercise.storage.entity.CatFactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CatFactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCatFact(catFact: CatFactEntity)

    @Query("SELECT * FROM cat_facts ORDER BY id DESC LIMIT 1")
    fun getLatestCatFact(): Flow<CatFactEntity?>
}