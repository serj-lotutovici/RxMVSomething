package com.serjltt.devfest.weather.show.forecast.presenter

import com.serjltt.devfest.weather.mvp.Presenter
import com.serjltt.devfest.weather.show.forecast.ForecastMvp
import com.serjltt.devfest.weather.show.forecast.usecase.GetForecastUseCase
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class ForecastPresenter @Inject internal constructor(
    private val getForecastUseCase: GetForecastUseCase)
  : Presenter<ForecastMvp.View> {

  override fun bind(view: ForecastMvp.View): Disposable {
    return view.cityName()
        .flatMap { getForecastUseCase.getForecast(it) }
        .subscribeBy({ view.updateView(it) })
  }
}
