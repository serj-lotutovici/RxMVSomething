package com.serjltt.devfest.weather.data;

import com.serjltt.devfest.weather.data.json.Astronomy;
import com.serjltt.devfest.weather.data.json.Forecast;
import com.serjltt.devfest.weather.data.json.ForecastPacket;
import com.serjltt.devfest.weather.data.json.Units;
import com.serjltt.devfest.weather.data.network.NetworkTestRule;
import io.reactivex.observers.TestObserver;
import okhttp3.mockwebserver.MockResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.serjltt.devfest.weather.TestUtils.fromResource;
import static org.assertj.core.api.Java6Assertions.assertThat;

public final class WeatherServiceTest {
  @Rule public NetworkTestRule testRule = new NetworkTestRule();

  WeatherService weatherService;

  @Before public void setUp() throws Exception {
    weatherService = testRule.create(WeatherService.class);
  }

  @Test public void serviceReturnsExpectedData() throws Exception {
    testRule.enqueue(new MockResponse().setBody(fromResource("yahoo_response.json")));

    TestObserver<ForecastPacket> testObserver = weatherService.getForecast("q", "f", "e").test();

    ForecastPacket packet = testObserver.assertNoErrors().values().get(0);

    // Just copy pasting expected values form the file
    assertThat(packet.getTitle()).isEqualTo("Yahoo! Weather - Amsterdam, NH, NL");
    assertThat(packet.getAstronomy()).isEqualTo(new Astronomy("6:0 am", "9:33 pm"));
    assertThat(packet.getUnits()).isEqualTo(new Units("mi", "in", "mph", "F"));
    // Not testing the whole list since the library under the hood is already tested
    assertThat(packet.getForecast()).hasSize(10);

    Forecast expected = new Forecast("30", "01 Aug 2016", "Mon", "69", "55", "Partly Cloudy");
    assertThat(packet.getForecast().get(2).equals(expected)).isTrue();
    assertThat(packet.getForecast().get(2).hashCode()).isEqualTo(expected.hashCode());
  }
}