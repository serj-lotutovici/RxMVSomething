package com.serjltt.devfest.weather.show.forecast.model

import android.os.Parcelable
import com.serjltt.devfest.weather.show.forecast.ForecastMvp
import java.io.Serializable

/**
 * Model that will be used to present the data. This class is not overloaded by extra data that is
 * received originally from the backend.
 *
 *
 * **DISCLAIMER: ** Implements [Serializable] for convenience, normally it would be
 * [Parcelable] which delegation of the whole boilerplate to AutoValue.
 */
data class ForecastModel(
    private val date: String,
    private val low: String,
    private val high: String)
  : ForecastMvp.Model, Serializable {

  override fun date(): String = date
  override fun lowestTemperature(): String = low
  override fun highestTemperature(): String = high
}
