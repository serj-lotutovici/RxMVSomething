package com.serjltt.devfest.weather.show.forecast

import com.serjltt.devfest.weather.mvp.Presenter
import com.serjltt.devfest.weather.mvp.StubPresenter
import dagger.Binds
import dagger.Module

@Module abstract class EspressoForecastModule {
  @Binds abstract fun provideForecastPresenter(stub: StubPresenter<ForecastView>)
      : Presenter<ForecastView>
}
