package com.serjltt.devfest.weather.data

import com.serjltt.devfest.weather.data.json.ForecastPacket
import com.serjltt.moshi.adapters.Wrapped
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/** Main service that provides the underlying data store. */
interface WeatherService {
  /*
   *
   * We will need to pass the following query params:
   * q=select * from weather.forecast where woeid in
   * (select woeid from geo.places(1) where text="amsterdam")
   * &format=json&env=store://datatables.org/alltableswithkeys
   *
   */
  @Wrapped(*arrayOf("query", "results", "channel"))
  @GET("/v1/public/yql")
  fun getForecast(@Query("q") query: String, @Query("format") format: String,
      @Query("env") env: String): Observable<ForecastPacket>
}
