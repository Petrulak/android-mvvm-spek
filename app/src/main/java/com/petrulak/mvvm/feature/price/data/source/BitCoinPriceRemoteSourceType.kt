package com.petrulak.mvvm.feature.price.data.source

import com.petrulak.mvvm.feature.price.data.model.PricesWrapperDto
import io.reactivex.Single
import retrofit2.http.GET


interface BitCoinPriceRemoteSourceType {

    @GET("/v1/bpi/currentprice.json")
    fun getPrices(): Single<PricesWrapperDto>

}