package com.petrulak.mvvm.feature.price.di

import com.petrulak.mvvm.common.SchedulerProviderType
import com.petrulak.mvvm.feature.price.data.BitCoinPriceRepository
import com.petrulak.mvvm.feature.price.data.BitCoinPriceRepositoryType
import com.petrulak.mvvm.feature.price.data.model.PricesMapper
import com.petrulak.mvvm.feature.price.data.source.BitCoinPriceLocalSource
import com.petrulak.mvvm.feature.price.data.source.BitCoinPriceLocalSourceType
import com.petrulak.mvvm.feature.price.data.source.BitCoinPriceRemoteSource
import com.petrulak.mvvm.feature.price.data.source.BitCoinPriceRemoteSourceType
import com.petrulak.mvvm.feature.price.domain.BitCoinPriceUseCase
import com.petrulak.mvvm.feature.price.domain.BitCoinPriceUseCaseType
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
class BitCoinPriceModule {

    @Singleton
    @Provides
    fun providePricesMapper(): PricesMapper = PricesMapper()

    @Singleton
    @Provides
    fun provideBitCoinPriceLocalSource(): BitCoinPriceLocalSourceType = BitCoinPriceLocalSource()

    @Singleton
    @Provides
    fun provideBitCoinPriceRemoteSource(retrofit: Retrofit): BitCoinPriceRemoteSourceType = BitCoinPriceRemoteSource(retrofit)

    @Singleton
    @Provides
    fun provideBitcoinPriceRepository(
        localSource: BitCoinPriceLocalSourceType,
        remoteSource: BitCoinPriceRemoteSourceType,
        mapper: PricesMapper
    ): BitCoinPriceRepositoryType = BitCoinPriceRepository(localSource, remoteSource, mapper)

    @Singleton
    @Provides
    fun provideBitCoinPriceUseCase(
        repositoryType: BitCoinPriceRepositoryType,
        schedulerProviderType: SchedulerProviderType
    ): BitCoinPriceUseCaseType = BitCoinPriceUseCase(repositoryType, schedulerProviderType)

}