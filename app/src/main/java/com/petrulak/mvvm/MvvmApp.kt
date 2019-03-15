package com.petrulak.mvvm

import android.app.Application
import com.petrulak.mvvm.di.DaggerMainComponent
import com.petrulak.mvvm.di.MainComponent
import com.petrulak.mvvm.di.MainModule
import com.petrulak.mvvm.feature.price.di.BitCoinPriceModule


class MvvmApp : Application() {

    val component: MainComponent by lazy {
        DaggerMainComponent.builder()
            .mainModule(MainModule(this))
            .bitCoinPriceModule(BitCoinPriceModule())
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }
}