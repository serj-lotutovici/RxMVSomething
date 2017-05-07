package com.serjltt.devfest.weather.di

import com.serjltt.devfest.weather.show.forecast.DaggerEspressoForecastUiComponent
import com.serjltt.devfest.weather.show.forecast.ForecastModule
import com.serjltt.devfest.weather.show.forecast.ForecastUiComponent

class EspressoInjector : Injector {
  override fun forecastUiComponent(forecastModule: ForecastModule): ForecastUiComponent =
      DaggerEspressoForecastUiComponent.builder().build()
}
