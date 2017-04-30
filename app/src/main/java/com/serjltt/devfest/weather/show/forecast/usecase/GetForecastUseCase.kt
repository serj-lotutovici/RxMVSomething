package com.serjltt.devfest.weather.show.forecast.usecase

import com.serjltt.devfest.weather.data.WeatherService
import com.serjltt.devfest.weather.show.forecast.ForecastMvp
import com.serjltt.devfest.weather.show.forecast.model.ForecastModel
import io.reactivex.Observable
import io.reactivex.rxkotlin.toObservable
import javax.inject.Inject

interface GetForecastUseCase {
  fun getForecast(city: String): Observable<List<ForecastMvp.Model>>
}

/**
 * This interactor like class is aware of all necessary parameters that the original data
 * [emitter][WeatherService] requires, others should not be concerned about that.
 */
internal class GetForecastUseCaseImpl @Inject internal constructor(
    private val weatherService: WeatherService)
  : GetForecastUseCase {

  override fun getForecast(city: String): Observable<List<ForecastMvp.Model>> {
    return weatherService.getForecast(querySelect(city), QUERY_FORMAT, QUERY_ENV)
        .flatMapSingle { packet ->
          packet.forecast.toObservable()
              .map<ForecastMvp.Model> { (_, date, _, high, low) ->
                ForecastModel(date, low, high)
              }
              .toList()
        }
  }

  private companion object {
    @JvmStatic fun querySelect(city: String): String {
      return "select * from weather.forecast " +
          "where woeid in (select woeid from geo.places(1) where text=\"$city\")"
    }

    private val QUERY_FORMAT = "json"
    private val QUERY_ENV = "store://datatables.org/alltableswithkeys"
  }
}
