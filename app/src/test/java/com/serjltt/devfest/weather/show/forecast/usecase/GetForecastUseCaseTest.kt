package com.serjltt.devfest.weather.show.forecast.usecase

import com.serjltt.devfest.weather.data.WeatherService
import com.serjltt.devfest.weather.data.network.NetworkTestRule
import com.serjltt.devfest.weather.fromResource
import com.serjltt.devfest.weather.show.forecast.model.ForecastModel
import okhttp3.mockwebserver.MockResponse
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetForecastUseCaseTest {
  @get:Rule var testRule = NetworkTestRule()

  private lateinit var useCase: GetForecastUseCase

  @Before fun setUp() {
    useCase = GetForecastUseCaseImpl(testRule.create(WeatherService::class))
  }

  @Test fun mapsPacketToModel() {
    testRule.enqueue(MockResponse().setBody(fromResource("yahoo_response.json")))

    val forecast = useCase.getForecast("any")
        .test()
        .assertNoErrors()
        .values()[0]

    assertThat(forecast).hasSize(10)
    assertThat(forecast[9]).isEqualToComparingFieldByField(ForecastModel("08 Aug 2016", "59", "70"))
  }
}
