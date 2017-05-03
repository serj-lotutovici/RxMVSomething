package com.serjltt.devfest.weather.show.forecast.usecase

import com.serjltt.devfest.weather.data.WeatherService
import com.serjltt.devfest.weather.rx.RxModule
import com.serjltt.devfest.weather.show.forecast.model.ForecastData
import com.serjltt.devfest.weather.show.forecast.model.ForecastModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.toObservable
import javax.inject.Inject
import javax.inject.Named

interface GetForecastUseCase {
  fun getForecast(city: String): Observable<ForecastModel>
}

/**
 * This interactor like class is aware of all necessary parameters that the original data
 * [emitter][WeatherService] requires, others should not be concerned about that.
 */
internal class GetForecastUseCaseImpl @Inject internal constructor(
    private val weatherService: WeatherService,
    @param:Named(RxModule.MAIN_SCHEDULER) private val mainThreadScheduler: Scheduler)
  : GetForecastUseCase {

  override fun getForecast(city: String): Observable<ForecastModel> {
    return weatherService.getForecast(querySelect(city), QUERY_FORMAT, QUERY_ENV)
        .flatMapSingle<ForecastModel> { packet ->
          packet.forecast.toObservable()
              .map<ForecastData> { (_, date, _, high, low) -> ForecastData(date, low, high) }
              .toList()
              .map { data -> ForecastModel.Success(data) }
        }
        .onErrorReturn { th -> ForecastModel.Error(th) }
        .observeOn(mainThreadScheduler)
        .startWith(ForecastModel.Progress)
  }

  private companion object {
    @JvmStatic fun querySelect(city: String): String {
      return "select * from weather.forecast " +
          "where woeid in (select woeid from geo.places(1) " +
          "where text=\"$city\")"
    }

    const val QUERY_FORMAT = "json"
    const val QUERY_ENV = "store://datatables.org/alltableswithkeys"
  }
}
