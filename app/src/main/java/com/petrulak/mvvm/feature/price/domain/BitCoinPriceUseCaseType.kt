package com.petrulak.mvvm.feature.price.domain

import com.petrulak.mvvm.feature.price.data.model.Prices
import io.reactivex.Completable
import io.reactivex.Observable


interface BitCoinPriceUseCaseType {

    fun pricesStream(): Observable<Prices>

    fun refresh(): Completable

}