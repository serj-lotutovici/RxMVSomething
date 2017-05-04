package com.serjltt.devfest.weather.rx

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.BiFunction

/** Wraps this value into an observable that emits and then terminates.  */
fun <T : Any> T.toObservable(): Observable<T> = Observable.just(this)

/**
 * Zips the initial observable with the provided observable source and combines their results
 * in a single pair.
 */
fun <F, S> Observable<F>.zipWithAsPair(other: ObservableSource<S>): Observable<Pair<F, S>> =
    zipWith(other, BiFunction { f, s -> Pair(f, s) })
