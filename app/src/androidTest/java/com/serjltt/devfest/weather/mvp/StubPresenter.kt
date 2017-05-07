package com.serjltt.devfest.weather.mvp

import io.reactivex.disposables.Disposable

/** A tub implementation that will substitute the presenter implementation for any view. */
class StubPresenter<T : Any> : Presenter<T> {
  override fun bind(view: T): Disposable {
    return object : Disposable {
      override fun isDisposed(): Boolean = true
      override fun dispose() = Unit
    }
  }
}
