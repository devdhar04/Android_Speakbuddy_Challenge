package jp.speakbuddy.edisonandroidexercise.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.speakbuddy.edisonandroidexercise.storage.utils.Constants.Companion.CAT_FACT_TABLE_NAME

@Entity(tableName = CAT_FACT_TABLE_NAME)
data class CatFactEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fact: String,
    val length: Int
)