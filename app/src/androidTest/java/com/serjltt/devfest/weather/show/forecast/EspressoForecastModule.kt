package com.serjltt.devfest.weather.show.forecast

import com.serjltt.devfest.weather.mvp.Presenter
import com.serjltt.devfest.weather.mvp.StubPresenter
import dagger.Module
import dagger.Provides

@Module class EspressoForecastModule {
  @Provides internal fun provideForecastPresenter(): Presenter<ForecastView> = StubPresenter()
}
