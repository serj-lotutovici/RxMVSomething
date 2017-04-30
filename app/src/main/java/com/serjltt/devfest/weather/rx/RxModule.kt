package com.serjltt.devfest.weather.rx

import com.serjltt.devfest.weather.di.Global
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Named

@Global @Module class RxModule(
    private val ioScheduler: Scheduler,
    private val mainScheduler: Scheduler) {

  interface Component {
    @Named(IO_SCHEDULER) fun ioScheduler(): Scheduler
    @Named(MAIN_SCHEDULER) fun mainScheduler(): Scheduler
  }

  @Provides @Named(IO_SCHEDULER) internal fun provideIoScheduler() = ioScheduler
  @Provides @Named(MAIN_SCHEDULER) internal fun provideMainScheduler() = mainScheduler

  companion object {
    const val IO_SCHEDULER = "io"
    const val MAIN_SCHEDULER = "main"
  }
}
