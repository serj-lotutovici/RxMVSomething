package com.serjltt.devfest.weather.data.json

import com.squareup.moshi.Json

data class Forecast(
    @Json(name = "code") val code: String,
    @Json(name = "date") val date: String,
    @Json(name = "day") val day: String,
    @Json(name = "high") val high: String,
    @Json(name = "low") val low: String,
    @Json(name = "text") val text: String
)
