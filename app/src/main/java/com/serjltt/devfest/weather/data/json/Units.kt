package com.serjltt.devfest.weather.data.json

import com.squareup.moshi.Json

data class Units(
    @Json(name = "distance") val distance: String,
    @Json(name = "pressure") val pressure: String,
    @Json(name = "speed") val speed: String,
    @Json(name = "temperature") val temperature: String
)
