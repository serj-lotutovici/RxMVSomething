package com.serjltt.devfest.weather.show.forecast

import dagger.Component

@Component(modules = arrayOf(EspressoForecastModule::class))
interface EspressoForecastUiComponent : ForecastUiComponent
