package com.petrulak.mvvm.feature.price.view

import com.petrulak.mvvm.MvvmApp
import com.petrulak.mvvm.common.TestSchedulerProvider
import com.petrulak.mvvm.common.hasOneValue
import com.petrulak.mvvm.common.haveNoValues
import com.petrulak.mvvm.di.MainComponent
import com.petrulak.mvvm.feature.price.data.model.Prices
import com.petrulak.mvvm.feature.price.data.model.newMock
import com.petrulak.mvvm.feature.price.domain.BitCoinPriceUseCaseType
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object BitCoinPriceViewModelTest : Spek({

    val pricesMock = Prices.newMock()

    val component = mock(MainComponent::class.java)
    val application = mock(MvvmApp::class.java).apply {
        `when`(this.component).thenReturn(component)
    }

    val useCase by memoized {
        mock(BitCoinPriceUseCaseType::class.java).apply {

            `when`(pricesStream()).thenReturn(Observable.just(pricesMock))

            `when`(refresh()).thenReturn(Completable.complete())
        }
    }

    val viewModel by memoized {
        BitCoinPriceViewModel(application).apply {
            bitCoinPriceUseCase = useCase
            schedulerProviderType = TestSchedulerProvider()
        }
    }

    val pricesObserver by memoized { TestObserver<Prices>() }
    val errorObserver by memoized { TestObserver<String>() }

    beforeEachTest {
        viewModel.outputs.pricesStream().subscribe(pricesObserver)
        viewModel.outputs.error().subscribe(errorObserver)
    }

    describe("onResume") {

        context("subscribing is successful") {

            beforeEach {
                viewModel.inputs.onResume()
            }

            it("prices observable should emit values") {
                pricesObserver.hasOneValue(pricesMock)
            }
        }

        context("subscribing throws an error") {

            beforeEach {
                `when`(useCase.pricesStream()).thenReturn(Observable.error(Throwable()))
                viewModel.inputs.onResume()
            }

            it("prices observable should not emit any values") {
                pricesObserver.haveNoValues()
            }

            it("error observable should emit an error") {
                errorObserver.assertValueCount(1)
            }
        }
    }

    describe("onRefresh") {

        context("refreshing is successful") {

            beforeEach {
                viewModel.inputs.onRefreshClicked()
            }

            it("should not emit new values") {
                pricesObserver.haveNoValues()
            }

            it("error observable should not emit an error") {
                errorObserver.haveNoValues()
            }
        }

        context("subscribing throws an error") {

            beforeEach {
                `when`(useCase.refresh()).thenReturn(Completable.error(Throwable()))
                viewModel.inputs.onRefreshClicked()
            }

            it("prices observable should not emit new values") {
                pricesObserver.haveNoValues()
            }

            it("error observable should emit an error") {
                errorObserver.assertValueCount(1)
            }
        }
    }
})