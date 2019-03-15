package com.petrulak.mvvm.common

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers


class TestSchedulerProvider : SchedulerProviderType {

    override fun io(): Scheduler = Schedulers.trampoline()
    override fun ui(): Scheduler = Schedulers.trampoline()
    override fun computation(): Scheduler = Schedulers.trampoline()
    override fun newThread(): Scheduler = Schedulers.trampoline()

}