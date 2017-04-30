package com.serjltt.devfest.weather.rx

import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.TestScheduler
import java.util.concurrent.TimeUnit

/** A scheduler that delegates to a {@link TestScheduler}, with the exception of executing the tasks immediately. */
class ImmediateTestScheduler : Scheduler() {
  private val delegate = TestScheduler()

  override fun createWorker(): Scheduler.Worker = TestWorker()

  private inner class TestWorker : Scheduler.Worker() {
    private val delegateWorker = delegate.createWorker()

    override fun schedule(run: Runnable, delay: Long, unit: TimeUnit): Disposable =
        delegateWorker.schedule(run, delay, unit).also { delegate.triggerActions() }

    override fun dispose() = delegateWorker.dispose()
    override fun isDisposed() = delegateWorker.isDisposed
  }
}
