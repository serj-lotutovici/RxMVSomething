package com.serjltt.devfest.weather

import com.serjltt.devfest.weather.data.network.NetworkModule
import com.serjltt.devfest.weather.di.DaggerInjector
import com.serjltt.devfest.weather.di.Injector
import com.serjltt.devfest.weather.di.InjectorApp
import com.serjltt.devfest.weather.di.PlatformModule
import com.serjltt.devfest.weather.rx.RxModule
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl

/** Main application class, that constructs the "real" di component.  */
class WeatherApp : InjectorApp() {
  override fun buildInjector(): Injector {
    return DaggerInjector.builder()
        .platformModule(PlatformModule(this))
        .networkModule(NetworkModule(REAL_ENDPOINT))
        .rxModule(RxModule(Schedulers.io(), AndroidSchedulers.mainThread()))
        .build()
  }

  companion object {
    private val REAL_ENDPOINT = HttpUrl.parse("https://query.yahooapis.com/")
  }
}
