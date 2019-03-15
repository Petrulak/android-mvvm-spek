package com.petrulak.mvvm.feature.price.data.model

import com.google.gson.annotations.SerializedName


data class PricesWrapperDto(
    val bpi: PricesDto?
) {
    companion object
}

data class PricesDto(
    @SerializedName("USD")
    val usd: PriceDto?,
    @SerializedName("GBP")
    val gbp: PriceDto?,
    @SerializedName("EUR")
    val eur: PriceDto?
) {
    companion object
}

data class Prices(
    val usd: Price?,
    val gbp: Price?,
    val eur: Price?
) {
    companion object
}

data class PriceDto(
    val code: String?,
    val symbol: String?,
    @SerializedName("rate_float")
    val rate: Float?,
    val description: String?
) {
    companion object
}

data class Price(
    val code: String,
    val symbol: String,
    val rate: Float,
    val description: String
) {
    companion object
}