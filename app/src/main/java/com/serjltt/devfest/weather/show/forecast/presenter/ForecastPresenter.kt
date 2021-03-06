package com.serjltt.devfest.weather.show.forecast.presenter

import com.f2prateek.rx.preferences2.Preference
import com.serjltt.devfest.weather.mvp.Presenter
import com.serjltt.devfest.weather.show.forecast.ForecastUiModel
import com.serjltt.devfest.weather.show.forecast.ForecastView
import com.serjltt.devfest.weather.show.forecast.usecase.GetForecastResult
import com.serjltt.devfest.weather.show.forecast.usecase.GetForecastUseCase
import com.serjltt.devfest.weather.show.forecast.usecase.SelectCityResult
import com.serjltt.devfest.weather.show.forecast.usecase.SelectCityUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class ForecastPresenter @Inject internal constructor(
    private val cityNamePref: Preference<String>,
    private val getForecastUseCase: GetForecastUseCase,
    private val selectCityUseCase: SelectCityUseCase)
  : Presenter<ForecastView> {

  override fun bind(view: ForecastView): Disposable {
    val disposables = CompositeDisposable()

    disposables += cityNamePref.asObservable()
        .flatMap { cityName ->
          getForecastUseCase.getForecast(cityName)
              .map { result ->
                when (result) {
                  is GetForecastResult.Success -> ForecastUiModel.Success(cityName, result.data)
                  is GetForecastResult.Error -> ForecastUiModel.Error(result.msg)
                  is GetForecastResult.Progress -> ForecastUiModel.Progress
                }
              }
        }
        .subscribeBy({ view.updateView(it) })

    disposables += view.selectCity()
        .flatMapSingle { selectCityUseCase.selectCity() }
        .subscribeBy({ result ->
          when (result) {
            is SelectCityResult.NewCity -> cityNamePref.set(result.name)
            is SelectCityResult.Canceled -> Unit
          }
        })

    return disposables

  }
}
