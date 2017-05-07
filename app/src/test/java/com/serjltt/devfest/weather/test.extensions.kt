package com.serjltt.devfest.weather

import okio.Okio
import org.mockito.Mockito
import org.mockito.Mockito.*

fun fromResource(file: String): String {
  val classLoader = Thread.currentThread().contextClassLoader
  classLoader.getResourceAsStream(file).use { `is` ->
    return Okio.buffer(Okio.source(`is`)).readUtf8()
  }
}

/** Mocks the return of a method of a mock. */
fun <R : Any> R?.mockReturn(value: R?): Unit {
  `when`(this).thenReturn(value)
}

/** Verifies certain behavior happened exact [times]. */
fun <T : Any> T.assertCalled(times: Int): T = verify(this, times(times))

/** Verifies certain behavior never happened. */
fun <T : Any> T.assertNeverCalled(): T = verify(this, never())

/** Casts this object to the expected type. */
inline fun <reified T : Any> Any.cast(): T =
    try {
      this as T
    } catch (ex: Throwable) {
      throw AssertionError("Expected $this to be ${T::class}")
    }

/**
 * Alternative to [Mockito]s [Mockito.any] method
 * that helps to avoid NPEs when testing interfaces defined in kotlin.
 */
fun <T> any(): T {
  Mockito.any<T>()
  return uninitialized()
}

private fun <T> uninitialized(): T = null as T
