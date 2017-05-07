package com.serjltt.devfest.weather.show.forecast.view

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.serjltt.devfest.weather.R
import com.serjltt.devfest.weather.espresso.RecyclerViewMatcher
import com.serjltt.devfest.weather.show.forecast.ForecastUiModel
import org.hamcrest.Matchers.allOf

/** Abstracts view assertions for [ForecastActivity]. */
internal class ForecastActivityScreen {
  fun assertProgressVisible() {
    onView(withId(R.id.forecast_progress)).checkIsVisible()
    onView(withId(R.id.forecast_error)).checkIsGone()
    onView(withId(R.id.forecast_list)).checkIsGone()
  }

  fun assertErrorVisible(errorText: String) {
    onView(withId(R.id.forecast_error))
        .checkIsVisible()
        .check(matches(withText(errorText)))

    onView(withId(R.id.forecast_progress)).checkIsGone()
    onView(withId(R.id.forecast_list)).checkIsGone()
  }

  fun assertSuccessVisible(success: ForecastUiModel.Success) {
    onView(
        allOf(
            isAssignableFrom(TextView::class.java),
            withParent(isAssignableFrom(Toolbar::class.java)))
    )
        .check(matches(withText("Weather: ${success.cityName}")))

    onView(withId(R.id.forecast_progress)).checkIsGone()
    onView(withId(R.id.forecast_error)).checkIsGone()
    onView(withId(R.id.forecast_list)).checkIsVisible()

    success.data.forEachIndexed { position, (date) ->
      onView(RecyclerViewMatcher.withRecyclerView(R.id.forecast_list)
          .atPositionOnView(position, R.id.item_date))
          .check(matches(withText(date)))
    }


  }

  fun clickOnFab() {
    onView(withId(R.id.forecast_fab)).perform(click())
  }

  private fun ViewInteraction.checkIsGone(): ViewInteraction = apply {
    check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
  }

  private fun ViewInteraction.checkIsVisible(): ViewInteraction = apply {
    check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
  }
}
