package com.petrulak.mvvm.feature.price.data.source

import android.util.Log
import com.petrulak.mvvm.feature.price.data.model.Prices
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class BitCoinPriceLocalSource : BitCoinPriceLocalSourceType {

    internal val cache = BehaviorSubject.create<Prices>()

    override fun pricesStream(): Observable<Prices> {
        return cache.distinctUntilChanged().hide()
    }

    override fun updatePrices(value: Prices): Completable {
        return Completable.fromCallable {
            if (value.eur != null && value.gbp != null && value.usd != null) {
                cache.onNext(value)
            } else {
                Log.e("Whops", "At least one of the : $value is null. Skipping")
            }
        }
    }
}