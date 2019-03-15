package com.petrulak.mvvm.feature.price.domain

import com.nhaarman.mockitokotlin2.verify
import com.petrulak.mvvm.common.TestSchedulerProvider
import com.petrulak.mvvm.common.hasOneValue
import com.petrulak.mvvm.feature.price.data.BitCoinPriceRepositoryType
import com.petrulak.mvvm.feature.price.data.model.Prices
import com.petrulak.mvvm.feature.price.data.model.newMock
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object BitCoinPriceUseCaseTest : Spek({

    val pricesMock = Prices.newMock()

    val repository by memoized {
        mock(BitCoinPriceRepositoryType::class.java).apply {

            `when`(refresh()).thenReturn(Completable.complete())

            `when`(pricesStream()).thenReturn(Observable.just(pricesMock))
        }
    }

    val useCase by memoized {
        BitCoinPriceUseCase(
            repository = repository,
            schedulerProvider = TestSchedulerProvider()
        )
    }

    val pricesObserver by memoized { TestObserver.create<Prices>() }
    val refreshObserver by memoized { TestObserver.create<Void>() }

    describe("refresh") {

        beforeEach {
            useCase.refresh().subscribe(refreshObserver)
        }

        it("should delegate to respoitory") {
            verify(repository).refresh()
        }
    }

    describe("pricesStream") {

        context("refresh is succesful") {

            beforeEach {
                useCase.pricesStream().subscribe(pricesObserver)
            }

            it("should emit last values") {
                pricesObserver.hasOneValue(pricesMock)
            }

            it("should refresh pricesStream") {
                verify(repository).refresh()
            }
        }

        context("refresh is NOT successful") {

            beforeEach {
                `when`(repository.refresh()).thenReturn(Completable.error(Throwable()))
                useCase.pricesStream().subscribe(pricesObserver)
            }

            it("should emit last values") {
                pricesObserver.hasOneValue(pricesMock)
            }

            it("should refresh pricesStream") {
                verify(repository).refresh()
            }
        }

    }

})