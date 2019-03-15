package com.petrulak.mvvm.feature.price.view

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.petrulak.mvvm.R
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_bitcoin_price.*


class BitCoinPriceActivity : FragmentActivity() {

    private val viewModel: BitCoinPriceViewModel by lazy {
        ViewModelProviders.of(this).get(BitCoinPriceViewModel::class.java)
    }

    private val subscriptions = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bitcoin_price)
    }

    override fun onResume() {
        super.onResume()

        viewModel.outputs.pricesStream()
            .subscribe {
                tv_eur.text = "EUR : ${it.eur?.rate.toString()}"
                tv_gbp.text = "GBP : ${it.gbp?.rate.toString()}"
                tv_usd.text = "USD : ${it.usd?.rate.toString()}"
            }
            .also { subscriptions.add(it) }

        viewModel.outputs.error()
            .subscribe { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
            .also { subscriptions.add(it) }

        button_refresh.setOnClickListener { viewModel.inputs.onRefreshClicked() }

        viewModel.inputs.onResume()
    }

    override fun onPause() {
        super.onPause()
        tv_eur.text = "EUR : ---"
        tv_gbp.text = "GBP : ---"
        tv_usd.text = "USD : ---"
        subscriptions.clear()
        viewModel.inputs.onPause()
    }
}