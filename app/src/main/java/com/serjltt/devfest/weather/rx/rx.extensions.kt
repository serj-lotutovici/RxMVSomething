package com.serjltt.devfest.weather.rx

import io.reactivex.Observable

/** Wraps this value into an observable that emits and then terminates.  */
fun <T : Any> T.toObservable(): Observable<T> = Observable.just(this)
