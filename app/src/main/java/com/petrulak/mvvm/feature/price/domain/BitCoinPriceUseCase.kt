package com.petrulak.mvvm.feature.price.domain

import android.util.Log
import com.petrulak.mvvm.common.SchedulerProviderType
import com.petrulak.mvvm.feature.price.data.BitCoinPriceRepositoryType
import com.petrulak.mvvm.feature.price.data.model.Prices
import io.reactivex.Completable
import io.reactivex.Observable

class BitCoinPriceUseCase(
    private val repository: BitCoinPriceRepositoryType,
    private val schedulerProvider: SchedulerProviderType
) : BitCoinPriceUseCaseType {

    override fun pricesStream(): Observable<Prices> {
        return repository.pricesStream()
            .doOnSubscribe { refresh().subscribe({}, { Log.e("Shit", "error fetching stuff") }) }
            .observeOn(schedulerProvider.ui())
            .subscribeOn(schedulerProvider.io())
    }

    override fun refresh(): Completable {
        return repository.refresh()
            .observeOn(schedulerProvider.ui())
            .subscribeOn(schedulerProvider.io())
    }
}