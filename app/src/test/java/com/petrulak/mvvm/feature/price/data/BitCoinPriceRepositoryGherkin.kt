package com.petrulak.mvvm.feature.price.data

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.petrulak.mvvm.common.hasCompletedWithError
import com.petrulak.mvvm.common.hasCompletedWithoutErrors
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
import org.spekframework.spek2.style.gherkin.Feature

object BitCoinPriceRepositoryGherkin : Spek({

    Feature("refresh") {

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

        val refreshObserver by memoized { TestObserver.create<Void>() }

        Scenario("new values are fetched from API") {

            When("fetching new values") {
                repository.refresh().subscribe(refreshObserver)
            }

            Then("should store new values") {
                verify(localSource).updatePrices(mappedValuesFromApi)
            }

            And("completable should complete") {
                refreshObserver.hasCompletedWithoutErrors()
            }
        }

        Scenario("API throws an error") {

            val apiError = Throwable("Whoops")

            Given("API throws an error") {
                `when`(remoteSource.getPrices()).thenReturn(Single.error(apiError))
            }

            When("fetching new values") {
                repository.refresh().subscribe(refreshObserver)
            }

            Then("should not store new values") {
                verify(localSource, never()).updatePrices(any())
            }

            And("completable should complete") {
                refreshObserver.hasCompletedWithError(apiError)
            }
        }
    }
})