package jp.speakbuddy.edisonandroidexercise.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import jp.speakbuddy.edisonandroidexercise.storage.dao.CatFactDao
import jp.speakbuddy.edisonandroidexercise.storage.entity.CatFactEntity

@Database(entities = [CatFactEntity::class], version = 1, exportSchema = false)
abstract class CatFactDatabase : RoomDatabase() {
    abstract fun catFactDao(): CatFactDao
}