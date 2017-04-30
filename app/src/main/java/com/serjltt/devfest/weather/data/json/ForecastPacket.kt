package com.serjltt.devfest.weather.data.json

import com.serjltt.moshi.adapters.Wrapped
import com.squareup.moshi.Json

data class ForecastPacket(
    @Json(name = "title") val title: String,
    @Json(name = "units") val units: Units,
    @Json(name = "astronomy") val astronomy: Astronomy,
    @Json(name = "item") @field:Wrapped(value = *arrayOf("forecast")) val forecast: List<Forecast>
)
