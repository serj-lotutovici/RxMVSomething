package com.serjltt.devfest.weather.di

import android.app.Application
import android.content.Context

/** Main contract of an application that keeps a reference to the di component.  */
abstract class InjectorApp : Application() {
  lateinit var injector: Injector

  override fun onCreate() {
    super.onCreate()
    injector = buildInjector()
  }

  /** Returns an instance of the injector.  */
  fun injector(): Injector {
    return injector
  }

  protected abstract fun buildInjector(): Injector

  companion object {
    /** Returns an instance of the underlying application.  */
    internal operator fun get(context: Context): InjectorApp {
      return context.applicationContext as InjectorApp
    }
  }
}
