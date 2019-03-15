package com.petrulak.mvvm.feature.price.data.model

fun PricesWrapperDto.Companion.newMock() = PricesWrapperDto(
    bpi = PricesDto.newMock()
)

fun PricesDto.Companion.newMock() = PricesDto(
    usd = PriceDto.newMock(),
    eur = PriceDto.newMock(),
    gbp = PriceDto.newMock()
)

fun PricesDto.Companion.newNullMock() = PricesDto(
    usd = null,
    eur = null,
    gbp = null
)

fun Prices.Companion.newMock() = Prices(
    usd = Price.newMock(),
    eur = Price.newMock(),
    gbp = Price.newMock()
)

fun Prices.Companion.newNullMock() = Prices(
    usd = null,
    eur = null,
    gbp = null
)

fun PriceDto.Companion.newMock() = PriceDto(
    code = "code",
    symbol = "symbol",
    rate = 1f,
    description = "description"
)

fun PriceDto.Companion.newNullMock() = PriceDto(
    code = null,
    symbol = null,
    rate = null,
    description = null
)

fun Price.Companion.newMock() = Price(
    code = "code",
    symbol = "symbol",
    rate = 1f,
    description = "description"
)