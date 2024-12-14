package jp.speakbuddy.edisonandroidexercise.utils

import jp.speakbuddy.edisonandroidexercise.network.model.FactResponse
import jp.speakbuddy.edisonandroidexercise.storage.entity.CatFactEntity

fun CatFactEntity.mapEntityToResponse(): FactResponse {
    return FactResponse(fact = this.fact, length = this.length)
}