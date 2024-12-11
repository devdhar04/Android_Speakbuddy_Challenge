package jp.speakbuddy.edisonandroidexercise.network.model

import kotlinx.serialization.Serializable

@Serializable
data class FactResponse(
    val fact: String,
    val length: Int = 0
) {
}