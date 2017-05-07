package com.serjltt.devfest.weather.show.forecast

import io.reactivex.Observable
import java.io.Serializable


/**
 * Model that will be used to present the state of the [ForecastView].
 */
sealed class ForecastUiModel : Serializable {
  data class Success(val cityName: String, val data: List<ForecastData>) : ForecastUiModel()
  data class Error(val message: String?) : ForecastUiModel()
  object Progress : ForecastUiModel()
}

/** A fin representation of [com.serjltt.devfest.weather.data.json.Forecast]. */
data class ForecastData(val date: String, val low: String, val high: String)

/** Forecast view contract. */
interface ForecastView {
  fun selectCity(): Observable<Unit>
  fun updateView(model: ForecastUiModel)
}
