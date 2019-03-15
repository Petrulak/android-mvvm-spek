package com.petrulak.mvvm.common

import io.reactivex.observers.TestObserver


fun <V> TestObserver<V>.hasOneValue(value: V) {
    this.assertValueCount(1)
        .assertNoErrors()
        .assertValue(value)
}

fun <V> TestObserver<V>.haveNoValues() {
    this.assertNoValues()
        .assertNoErrors()
}

fun <V> TestObserver<V>.hasCompletedWithoutErrors() {
    this.assertComplete()
        .assertNoErrors()
}

fun <V, T : Throwable> TestObserver<V>.hasCompletedWithError(throwable: T) {
    this.assertError(throwable)
        .onComplete()
}