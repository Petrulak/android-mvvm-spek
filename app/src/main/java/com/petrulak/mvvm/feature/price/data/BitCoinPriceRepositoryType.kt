package com.petrulak.mvvm.feature.price.data

import com.petrulak.mvvm.feature.price.data.model.Prices
import io.reactivex.Completable
import io.reactivex.Observable


interface BitCoinPriceRepositoryType {

    fun pricesStream(): Observable<Prices>

    fun refresh(): Completable
}