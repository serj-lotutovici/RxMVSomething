package com.serjltt.devfest.weather.mvp

import io.reactivex.disposables.Disposable

/** Base contract for a Presenter in a MVP implementation.  */
interface Presenter<T> {
  /** Binds a view and returns a disposables.  */
  fun bind(view: T): Disposable
}
