package jp.speakbuddy.edisonandroidexercise.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cat_facts")
data class CatFactEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fact: String,
    val length: Int
)