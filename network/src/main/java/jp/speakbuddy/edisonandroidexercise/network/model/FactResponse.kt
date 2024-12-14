package jp.speakbuddy.edisonandroidexercise.network.model

data class FactResponse(
    val fact: String,
    val length: Int = 0
) {
}