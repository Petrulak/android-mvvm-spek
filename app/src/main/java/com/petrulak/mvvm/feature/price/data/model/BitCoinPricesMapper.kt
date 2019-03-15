package com.petrulak.mvvm.feature.price.data.model

import com.petrulak.mvvm.common.Mapper


class PriceMapper : Mapper<PriceDto, Price>() {

    override fun map(from: PriceDto): Price {
        return with(from) {
            Price(
                code = code ?: EMPTY_STRING,
                description = description ?: EMPTY_STRING,
                rate = rate ?: INVALID_FLOAT,
                symbol = symbol ?: EMPTY_STRING
            )
        }
    }
}

class PricesMapper : Mapper<PricesWrapperDto, Prices>() {

    private val priceMapper = PriceMapper()

    override fun map(from: PricesWrapperDto): Prices {
        return with(from) {
            Prices(
                eur = bpi?.eur?.let { priceMapper.map(it) },
                gbp = bpi?.gbp?.let { priceMapper.map(it) },
                usd = bpi?.usd?.let { priceMapper.map(it) }
            )
        }
    }
}

