package com.serjltt.devfest.weather.di

import com.serjltt.devfest.weather.data.DataModule
import com.serjltt.devfest.weather.data.network.NetworkModule
import com.serjltt.devfest.weather.rx.RxModule
import com.serjltt.devfest.weather.show.forecast.ForecastModule
import com.serjltt.devfest.weather.show.forecast.ForecastUiComponent
import dagger.Component

/** Applications global injector.  */
@Global
@Component(
    modules = arrayOf(
        PlatformModule::class,
        NetworkModule::class,
        DataModule::class,
        RxModule::class)
)
interface Injector {
  fun forecastUiComponent(forecastModule: ForecastModule): ForecastUiComponent
}
