package com.serjltt.devfest.weather.rx

import com.f2prateek.rx.preferences2.Preference
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject

/** A simple implementation of a [Preference] backed by a [BehaviorSubject]. */
class StubPreference<T> : Preference<T> {
  private val preferenceSubject: BehaviorSubject<T> = BehaviorSubject.create()

  override fun set(value: T) = preferenceSubject.onNext(value)
  override fun asConsumer(): Consumer<in T> = Consumer { t -> preferenceSubject.onNext(t) }
  override fun asObservable(): Observable<T> = preferenceSubject
  override fun get(): T = preferenceSubject.value

  override fun isSet(): Boolean {
    throw UnsupportedOperationException()
  }

  override fun defaultValue(): T {
    throw UnsupportedOperationException()
  }

  override fun delete() {
    throw UnsupportedOperationException()
  }

  override fun key(): String {
    throw UnsupportedOperationException()
  }
}
