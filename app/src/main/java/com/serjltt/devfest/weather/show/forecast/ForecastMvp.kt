package com.serjltt.devfest.weather.show.forecast

import io.reactivex.Observable

/** General contracts for show forecast feature.  */
interface ForecastMvp {
  interface Model {
    fun date(): String
    fun lowestTemperature(): String
    fun highestTemperature(): String
  }

  interface View {
    fun cityName(): Observable<String>
    fun showLoading()
    fun hideLoading()
    fun showError(error: String?)
    fun showForecast(data: List<Model>)
  }
}
