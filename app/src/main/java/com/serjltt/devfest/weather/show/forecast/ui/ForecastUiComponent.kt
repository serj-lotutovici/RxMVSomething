package com.serjltt.devfest.weather.show.forecast.ui

import com.serjltt.devfest.weather.di.Consumer
import com.serjltt.devfest.weather.di.Injector
import com.serjltt.devfest.weather.show.forecast.ForecastBindModule
import com.serjltt.devfest.weather.show.forecast.ForecastModule
import dagger.Component

@Consumer
@Component(
    dependencies = arrayOf(Injector::class),
    modules = arrayOf(ForecastBindModule::class, ForecastModule::class)
)
internal interface ForecastUiComponent {
  fun inject(activity: ForecastActivity)
}
