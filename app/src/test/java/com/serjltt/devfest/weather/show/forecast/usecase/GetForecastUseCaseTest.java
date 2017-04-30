package com.serjltt.devfest.weather.show.forecast.usecase;

import com.serjltt.devfest.weather.data.WeatherService;
import com.serjltt.devfest.weather.data.network.NetworkTestRule;
import com.serjltt.devfest.weather.show.forecast.ForecastMvp;
import com.serjltt.devfest.weather.show.forecast.model.ForecastModel;
import io.reactivex.observers.TestObserver;
import java.util.List;
import okhttp3.mockwebserver.MockResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.serjltt.devfest.weather.TestUtils.fromResource;
import static org.assertj.core.api.Java6Assertions.assertThat;

public final class GetForecastUseCaseTest {
  @Rule public NetworkTestRule testRule = new NetworkTestRule();

  GetForecastUseCase useCase;

  @Before public void setUp() throws Exception {
    useCase = new GetForecastUseCaseImpl(testRule.create(WeatherService.class));
  }

  @Test public void mapsPacketToModel() throws Exception {
    testRule.enqueue(new MockResponse().setBody(fromResource("yahoo_response.json")));

    TestObserver<List<ForecastMvp.Model>> testObserver = useCase.getForecast("any").test();

    List<ForecastMvp.Model> forecast = testObserver.assertNoErrors().values().get(0);

    assertThat(forecast).hasSize(10);
    assertThat(forecast.get(9)).isEqualToComparingFieldByField(
        new ForecastModel("08 Aug 2016", "59", "70"));
  }
}
