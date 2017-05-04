package com.serjltt.devfest.weather.data

import com.serjltt.devfest.weather.data.json.Astronomy
import com.serjltt.devfest.weather.data.json.Forecast
import com.serjltt.devfest.weather.data.json.Units
import com.serjltt.devfest.weather.data.network.NetworkTestRule
import com.serjltt.devfest.weather.fromResource
import okhttp3.mockwebserver.MockResponse
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@Suppress("IllegalIdentifier")
class WeatherServiceTest {
  @get:Rule var testRule = NetworkTestRule()

  private lateinit var weatherService: WeatherService

  @Before fun setUp() {
    weatherService = testRule.create(WeatherService::class)
  }

  @Test fun `Service success case is a ForecastPacket`() {
    testRule.enqueue(MockResponse().setBody(fromResource("yahoo_response.json")))

    val (title, units, astronomy, forecast) = weatherService.getForecast("q", "f", "e")
        .test()
        .assertNoErrors()
        .values()[0]

    // Just copy pasting expected values form the file
    assertThat(title).isEqualTo("Yahoo! Weather - Amsterdam, NH, NL")
    assertThat(astronomy).isEqualTo(Astronomy("6:0 am", "9:33 pm"))
    assertThat(units).isEqualTo(Units("mi", "in", "mph", "F"))
    // Not testing the whole list since the library under the hood is already tested.
    assertThat(forecast).hasSize(10)

    val expected = Forecast("30", "01 Aug 2016", "Mon", "69", "55", "Partly Cloudy")
    assertThat(forecast[2] == expected).isTrue()
    assertThat(forecast[2].hashCode()).isEqualTo(expected.hashCode())
  }
}
