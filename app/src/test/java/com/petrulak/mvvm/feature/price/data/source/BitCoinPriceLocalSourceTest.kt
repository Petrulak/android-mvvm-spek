package com.petrulak.mvvm.feature.price.data.source

import com.petrulak.mvvm.feature.price.data.model.Prices
import com.petrulak.mvvm.feature.price.data.model.newMock
import com.petrulak.mvvm.feature.price.data.model.newNullMock
import com.petrulak.mvvm.common.hasCompletedWithoutErrors
import com.petrulak.mvvm.common.hasOneValue
import com.petrulak.mvvm.common.haveNoValues
import io.reactivex.observers.TestObserver
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object BitCoinPriceLocalSourceTest : Spek({

    val localSource by memoized { BitCoinPriceLocalSource() }
    val pricesMock by memoized { Prices.newMock() }
    val pricesNullMock by memoized { Prices.newNullMock() }

    val pricesObserver by memoized { TestObserver.create<Prices>() }
    val refreshObserver by memoized { TestObserver.create<Void>() }

    beforeEachTest {
        localSource.pricesStream().subscribe(pricesObserver)
    }

    describe("updatePrices") {

        context("some of the currencies are null") {

            beforeEach {
                localSource.updatePrices(pricesNullMock).subscribe(refreshObserver)
            }

            it("should not emit new values") {
                pricesObserver.haveNoValues()
            }

            it("completable should complete") {
                refreshObserver.hasCompletedWithoutErrors()
            }
        }

        context("all of the currencies are not null") {

            beforeEach {
                localSource.updatePrices(pricesMock).subscribe(refreshObserver)
            }

            it("should emit new values") {
                pricesObserver.hasOneValue(pricesMock)
            }

            it("completable should complete") {
                refreshObserver.hasCompletedWithoutErrors()
            }
        }
    }

    describe("pricesStream") {

        context("pricesStream have been set") {

            beforeEachTest {
                localSource.cache.onNext(pricesMock)
            }

            it("should not emit any values") {
                pricesObserver.hasOneValue(pricesMock)
            }

        }

        context("pricesStream have not been set") {

            it("should not emit any values") {
                pricesObserver.haveNoValues()
            }
        }
    }
})