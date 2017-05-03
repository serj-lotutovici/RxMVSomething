package com.serjltt.devfest.weather.show.forecast.model

import android.os.Parcelable
import java.io.Serializable

/**
 * Model that will be used to present the data. This class is not overloaded by extra data that is
 * received originally from the backend.
 *
 *
 * **DISCLAIMER: ** Implements [Serializable] for convenience, normally it would be
 * [Parcelable] with delegation of the whole boilerplate to AutoValue.
 */
sealed class ForecastModel : Serializable {
  data class Success(val data: List<ForecastData>) : ForecastModel()
  data class Error(val throwable: Throwable) : ForecastModel()
  object Progress : ForecastModel()
}
