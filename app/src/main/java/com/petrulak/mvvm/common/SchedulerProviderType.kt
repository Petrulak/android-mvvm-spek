package com.petrulak.mvvm.common

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton


interface SchedulerProviderType {

    fun io(): Scheduler
    fun ui(): Scheduler
    fun computation(): Scheduler
    fun newThread(): Scheduler
}

@Singleton
class SchedulerProvider @Inject constructor() : SchedulerProviderType {

    override fun io(): Scheduler = Schedulers.io()
    override fun ui(): Scheduler = AndroidSchedulers.mainThread()
    override fun computation() = Schedulers.computation()
    override fun newThread() = Schedulers.newThread()

}