package com.petrulak.mvvm.feature.price.data.source

import com.petrulak.mvvm.feature.price.data.model.PricesWrapperDto
import io.reactivex.Single
import retrofit2.Retrofit

class BitCoinPriceRemoteSource(retrofit: Retrofit) : BitCoinPriceRemoteSourceType {

    private val service = retrofit.create(BitCoinPriceRemoteSourceType::class.java)

    override fun getPrices(): Single<PricesWrapperDto> {
        return service.getPrices()
    }
}