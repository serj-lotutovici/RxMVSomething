package com.serjltt.devfest.weather.show.forecast.usecase

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import com.serjltt.devfest.weather.show.forecast.view.ForecastActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SelectCityUseCaseTest {
  @get:Rule val activityRule = ActivityTestRule(ForecastActivity::class.java)

  private lateinit var useCase: SelectCityUseCase

  @Before fun setUp() {
    useCase = SelectCityUseCaseImpl(activityRule.activity, AndroidSchedulers.mainThread())
  }

  @Test fun yieldsNewCityResult() {
    arrayOf("hamburg", "amsterdam", "barcelona").forEach { city ->
      val testObserver = useCase.selectCity()
          .test()

      onView(withText(city)).check(matches(isDisplayed()))
      onView(withText(city)).perform(click())

      testObserver
          .assertNoErrors()
          .assertValues(SelectCityResult.NewCity(city))
    }
  }

  @Test fun yieldsCanceledResult() {
    val testObserver = useCase.selectCity()
        .test()

    pressBack()

    testObserver
        .assertNoErrors()
        .assertValue(SelectCityResult.Canceled)
  }
}
