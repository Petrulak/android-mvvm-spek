package com.petrulak.mvvm.feature.price.data.source

import com.nhaarman.mockitokotlin2.verify
import com.petrulak.mvvm.common.hasOneValue
import com.petrulak.mvvm.feature.price.data.model.PricesWrapperDto
import com.petrulak.mvvm.feature.price.data.model.newMock
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import retrofit2.Retrofit

object BitCoinPriceRemoteSourceTest : Spek({

    val pricesWrapperDtoMock = PricesWrapperDto.newMock()

    val service = mock(BitCoinPriceRemoteSourceType::class.java).apply {
        `when`(getPrices()).thenReturn(Single.just(pricesWrapperDtoMock))
    }

    val retrofit by memoized {
        mock(Retrofit::class.java).apply {
            `when`(create(BitCoinPriceRemoteSourceType::class.java)).thenReturn(service)
        }
    }

    val remoteSource by memoized { BitCoinPriceRemoteSource(retrofit) }

    val pricesObserver by memoized { TestObserver<PricesWrapperDto>() }

    describe("getPrices") {

        beforeEach {
            remoteSource.getPrices().subscribe(pricesObserver)
        }

        it("should delegate call to service") {
            verify(service).getPrices()
        }

        it("should emit proper value") {
            pricesObserver.hasOneValue(pricesWrapperDtoMock)
        }
    }
})