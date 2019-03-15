package com.petrulak.mvvm.feature.price.data.source

import com.petrulak.mvvm.feature.price.data.model.Prices
import io.reactivex.Completable
import io.reactivex.Observable


interface BitCoinPriceLocalSourceType {

    fun pricesStream(): Observable<Prices>

    fun updatePrices(value: Prices): Completable
}