package com.serjltt.devfest.weather.show.forecast

import com.serjltt.devfest.weather.show.forecast.model.ForecastModel
import io.reactivex.Observable

/** General contracts for show forecast feature.  */
interface ForecastMvp {
  interface View {
    fun cityName(): Observable<String>
    fun updateView(model: ForecastModel)
  }
}
