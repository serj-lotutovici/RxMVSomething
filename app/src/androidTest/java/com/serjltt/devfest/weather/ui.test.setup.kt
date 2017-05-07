package com.serjltt.devfest.weather

import android.app.Application
import android.app.Instrumentation
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner
import com.serjltt.devfest.weather.di.EspressoInjector
import com.serjltt.devfest.weather.di.Injector
import com.serjltt.devfest.weather.di.InjectorApp

/**
 * This way the functional tests will use an application that provides a mock implementation of our
 * injector.
 */
class UIUnitTestRunner : AndroidJUnitRunner() {
  override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
    return Instrumentation.newApplication(UIUnitTestApp::class.java, context)
  }
}

class UIUnitTestApp : InjectorApp() {
  override fun buildInjector(): Injector = EspressoInjector()
}
