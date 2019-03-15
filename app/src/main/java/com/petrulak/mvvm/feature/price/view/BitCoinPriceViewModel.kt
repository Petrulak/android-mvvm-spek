package com.petrulak.mvvm.feature.price.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.petrulak.mvvm.MvvmApp
import com.petrulak.mvvm.common.SchedulerProviderType
import com.petrulak.mvvm.feature.price.data.model.Prices
import com.petrulak.mvvm.feature.price.domain.BitCoinPriceUseCaseType
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject


interface Inputs {
    fun onResume()
    fun onPause()
    fun onRefreshClicked()
}

interface Outputs {
    fun pricesStream(): Observable<Prices>
    fun error(): Observable<String>
}

class BitCoinPriceViewModel(application: Application) : Inputs, Outputs, AndroidViewModel(application) {

    val inputs: Inputs = this
    val outputs: Outputs = this

    @Inject lateinit var schedulerProviderType: SchedulerProviderType
    @Inject lateinit var bitCoinPriceUseCase: BitCoinPriceUseCaseType

    protected val error = PublishSubject.create<String>()
    protected val prices = PublishSubject.create<Prices>()

    private val subscriptions = CompositeDisposable()

    init {
        (application as MvvmApp).component.inject(this)
    }

    override fun pricesStream(): Observable<Prices> = prices.observeOn(schedulerProviderType.ui()).hide()

    override fun error(): Observable<String> = error.observeOn(schedulerProviderType.ui()).hide()

    override fun onRefreshClicked() {
        bitCoinPriceUseCase.refresh()
            .subscribe(
                { /* not interesed in success*/ },
                { error.onNext("Wups ERROR") }
            )
            .also { subscriptions.add(it) }
    }

    override fun onResume() {
        bitCoinPriceUseCase.pricesStream()
            .subscribe(
                { prices.onNext(it) },
                { error.onNext("Wups ERROR") }
            )
            .also { subscriptions.add(it) }
    }

    override fun onPause() {
        subscriptions.clear()
    }
}