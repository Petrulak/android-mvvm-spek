package com.petrulak.mvvm.di

import com.petrulak.mvvm.MvvmApp
import com.petrulak.mvvm.feature.price.di.BitCoinPriceModule
import com.petrulak.mvvm.feature.price.view.BitCoinPriceViewModel
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        MainModule::class,
        BitCoinPriceModule::class
    ]
)

interface MainComponent {

    fun inject(item: MvvmApp)
    fun inject(item: BitCoinPriceViewModel)

}