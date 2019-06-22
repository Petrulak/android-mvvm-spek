package com.petrulak.mvvm.feature.price.data

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.petrulak.mvvm.common.hasCompletedWithError
import com.petrulak.mvvm.common.hasCompletedWithoutErrors
import com.petrulak.mvvm.common.hasOneValue
import com.petrulak.mvvm.feature.price.data.model.Prices
import com.petrulak.mvvm.feature.price.data.model.PricesMapper
import com.petrulak.mvvm.feature.price.data.model.PricesWrapperDto
import com.petrulak.mvvm.feature.price.data.model.newMock
import com.petrulak.mvvm.feature.price.data.source.BitCoinPriceLocalSourceType
import com.petrulak.mvvm.feature.price.data.source.BitCoinPriceRemoteSourceType
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object BitCoinPriceRepositoryTest : Spek({

    val pricesWrapperDtoMock = PricesWrapperDto.newMock()
    val pricesMock = Prices.newMock()

    val pricesMapper = PricesMapper()
    val mappedValuesFromApi = pricesMapper.map(pricesWrapperDtoMock)

    val remoteSource by memoized {
        mock(BitCoinPriceRemoteSourceType::class.java).apply {
            `when`(getPrices()).thenReturn(Single.just(pricesWrapperDtoMock))
        }
    }

    val localSource by memoized {
        mock(BitCoinPriceLocalSourceType::class.java).apply {

            `when`(updatePrices(any())).thenReturn(Completable.complete())

            `when`(pricesStream()).thenReturn(Observable.just(pricesMock))
        }
    }

    val repository by memoized {
        BitCoinPriceRepository(
            remoteSource = remoteSource,
            localSource = localSource,
            pricesMapper = pricesMapper
        )
    }

    val pricesObserver by memoized { TestObserver.create<Prices>() }
    val refreshObserver by memoized { TestObserver.create<Void>() }

    describe("pricesStream") {

        beforeEach {
            repository.pricesStream().subscribe(pricesObserver)
        }

        it("should emit values from local source") {
            pricesObserver.hasOneValue(pricesMock)
            verify(localSource).pricesStream()
        }
    }

    describe("refresh") {

        context("new values are fetched from API and updating local source is successful ") {

            beforeEach {
                repository.refresh().subscribe(refreshObserver)
            }

            it("should store new values") {
                verify(localSource).updatePrices(mappedValuesFromApi)
            }

            it("should complete") {
                refreshObserver.hasCompletedWithoutErrors()
            }
        }

        context("new values are fetched from API and updating local source is NOT successful ") {

            val localSourceError = Throwable("WTF ???")

            beforeEach {
                `when`(localSource.updatePrices(any())).thenReturn(Completable.error(localSourceError))
                repository.refresh().subscribe(refreshObserver)
            }

            it("should store new values") {
                verify(localSource).updatePrices(mappedValuesFromApi)
            }

            it("should throw an error") {
                refreshObserver.hasCompletedWithError(localSourceError)
            }
        }

        context("API throws an error") {

            val apiError = Throwable("Whoops")

            beforeEach {
                `when`(remoteSource.getPrices()).thenReturn(Single.error(apiError))
                repository.refresh().subscribe(refreshObserver)
            }

            it("should not store new values") {
                verify(localSource, never()).updatePrices(pricesMock)
            }

            it("should throw an error") {
                refreshObserver.hasCompletedWithError(apiError)
            }
        }
    }
})