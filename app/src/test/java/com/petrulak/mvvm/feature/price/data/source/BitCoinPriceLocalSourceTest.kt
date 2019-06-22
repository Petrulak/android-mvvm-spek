package com.petrulak.mvvm.feature.price.data.source

import com.petrulak.mvvm.common.hasCompletedWithoutErrors
import com.petrulak.mvvm.common.hasOneValue
import com.petrulak.mvvm.common.haveNoValues
import com.petrulak.mvvm.feature.price.data.model.Prices
import com.petrulak.mvvm.feature.price.data.model.newMock
import com.petrulak.mvvm.feature.price.data.model.newNullMock
import io.reactivex.observers.TestObserver
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object BitCoinPriceLocalSourceTest : Spek({

    val pricesMock = Prices.newMock()
    val pricesNullMock = Prices.newNullMock()

    val localSource by memoized { BitCoinPriceLocalSource() }

    val pricesObserver by memoized { TestObserver.create<Prices>() }
    val refreshObserver by memoized { TestObserver.create<Void>() }

    beforeEachTest {
        localSource.pricesStream().subscribe(pricesObserver)
    }

    describe("updatePrices") {

        beforeEachTest {
            localSource.pricesStream().subscribe(pricesObserver)
        }

        context("some of the currencies are null") {

            beforeEach {
                localSource.updatePrices(pricesNullMock).subscribe(refreshObserver)
            }

            it("should not emit new values") {
                pricesObserver.haveNoValues()
            }

            it("refreshing should complete") {
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

            it("refreshing should complete") {
                refreshObserver.hasCompletedWithoutErrors()
            }
        }
    }

    describe("pricesStream") {

        context("prices have been set") {

            beforeEachTest {
                localSource.cache.onNext(pricesMock)
            }

            it("should emit last values") {
                pricesObserver.hasOneValue(pricesMock)
            }
        }

        context("prices have not been set") {

            it("should not emit any values") {
                pricesObserver.haveNoValues()
            }
        }
    }
})