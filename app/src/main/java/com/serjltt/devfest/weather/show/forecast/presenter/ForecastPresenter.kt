package com.serjltt.devfest.weather.show.forecast.presenter

import com.serjltt.devfest.weather.mvp.Presenter
import com.serjltt.devfest.weather.rx.RxModule
import com.serjltt.devfest.weather.show.forecast.ForecastMvp
import com.serjltt.devfest.weather.show.forecast.usecase.GetForecastUseCase
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import javax.inject.Inject
import javax.inject.Named

class ForecastPresenter @Inject internal constructor(
    private val getForecastUseCase: GetForecastUseCase,
    @param:Named(RxModule.IO_SCHEDULER) private val ioScheduler: Scheduler,
    @param:Named(RxModule.MAIN_SCHEDULER) private val mainThreadScheduler: Scheduler)
  : Presenter<ForecastMvp.View> {

  override fun bind(view: ForecastMvp.View): Disposable {
    val forecastObservable = view.cityName()
        .doOnNext { _ -> view.showLoading() }
        .flatMap<List<ForecastMvp.Model>> { getForecastUseCase.getForecast(it) }
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .doOnNext { _ -> view.hideLoading() }

    return forecastObservable.subscribe(
        { view.showForecast(it) },
        { throwable ->
          view.hideLoading()
          view.showError(throwable.message)
        })
  }
}
