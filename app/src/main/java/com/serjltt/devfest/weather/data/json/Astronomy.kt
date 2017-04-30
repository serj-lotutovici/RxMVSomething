package com.serjltt.devfest.weather.data.json

import com.squareup.moshi.Json

data class Astronomy(
    @Json(name = "sunrise") val sunrise: String?,
    @Json(name = "sunset") val sunset: String?
)
