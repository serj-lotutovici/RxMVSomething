package com.serjltt.devfest.weather

import android.annotation.SuppressLint
import io.reactivex.Observable
import okio.Okio
import org.mockito.Mockito.*

// suppressing warning since all test will run under java 7 or 8
@SuppressLint("NewApi")
@Throws(Exception::class) fun fromResource(file: String): String {
  val classLoader = Thread.currentThread().contextClassLoader
  classLoader.getResourceAsStream(file).use { `is` ->
    return Okio.buffer(Okio.source(`is`)).readUtf8()
  }
}

/** Wraps this value into an observable that emits and then terminates.  */
fun <T : Any> T.toJustObservable(): Observable<T> = Observable.just(this)

/** Mocks the return of a method of a mock. */
fun <R : Any> R?.mockReturn(value: R?): Unit {
  `when`(this).thenReturn(value)
}

/** Verifies certain behavior happened exact [times]. */
fun <T : Any> T.assertCalled(times: Int): T = verify(this, times(times))

/** Verifies certain behavior never happened. */
fun <T : Any> T.assertNeverCalled(): T = verify(this, never())