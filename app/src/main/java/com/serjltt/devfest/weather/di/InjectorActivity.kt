package com.serjltt.devfest.weather.di

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity

/** Base contract for activities that consume the main di component. */
abstract class InjectorActivity : AppCompatActivity() {
  @CallSuper override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val injector = InjectorApp[this].injector()
    onInject(injector)
  }

  /** Called when the activity has obtained the injector.  */
  protected abstract fun onInject(injector: Injector)
}
