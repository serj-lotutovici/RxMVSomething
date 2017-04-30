package com.serjltt.devfest.weather.show.forecast

import com.serjltt.devfest.weather.di.Consumer
import com.serjltt.devfest.weather.mvp.Presenter
import com.serjltt.devfest.weather.show.forecast.presenter.ForecastPresenter
import com.serjltt.devfest.weather.show.forecast.usecase.GetForecastUseCase
import com.serjltt.devfest.weather.show.forecast.usecase.GetForecastUseCaseImpl
import dagger.Binds
import dagger.Module

@Consumer @Module abstract class ForecastBindModule {
  @Binds internal abstract fun bindPresenter(presenter: ForecastPresenter)
      : Presenter<ForecastMvp.View>

  @Binds internal abstract fun bindUseCase(useCase: GetForecastUseCaseImpl)
      : GetForecastUseCase
}
