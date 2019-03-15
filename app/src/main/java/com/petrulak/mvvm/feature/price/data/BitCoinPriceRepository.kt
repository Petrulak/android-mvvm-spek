package com.petrulak.mvvm.feature.price.data

import com.petrulak.mvvm.feature.price.data.model.Prices
import com.petrulak.mvvm.feature.price.data.model.PricesMapper
import com.petrulak.mvvm.feature.price.data.source.BitCoinPriceLocalSourceType
import com.petrulak.mvvm.feature.price.data.source.BitCoinPriceRemoteSourceType
import io.reactivex.Completable
import io.reactivex.Observable

class BitCoinPriceRepository(
    private val localSource: BitCoinPriceLocalSourceType,
    private val remoteSource: BitCoinPriceRemoteSourceType,
    private val pricesMapper: PricesMapper
) : BitCoinPriceRepositoryType {

    override fun pricesStream(): Observable<Prices> {
        return localSource.pricesStream()
    }

    override fun refresh(): Completable {
        return remoteSource.getPrices()
            .map { pricesMapper.map(it) }
            .flatMapCompletable { localSource.updatePrices(it) }
    }
}