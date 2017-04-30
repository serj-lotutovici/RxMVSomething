package com.serjltt.devfest.weather.show.forecast.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.pedrogomez.renderers.Renderer
import com.serjltt.devfest.weather.R
import com.serjltt.devfest.weather.show.forecast.ForecastMvp

internal class ModelRenderer : Renderer<ForecastMvp.Model>() {
  @BindView(R.id.item_date) lateinit var dateView: TextView
  @BindView(R.id.item_low) lateinit var lowView: TextView
  @BindView(R.id.item_high) lateinit var highView: TextView

  override fun inflate(inflater: LayoutInflater, parent: ViewGroup): View =
      inflater.inflate(R.layout.list_item_forecast, parent, false).apply {
        ButterKnife.bind(this@ModelRenderer, this)
      }

  override fun render() {
    val model = content
    dateView.text = model.date()
    lowView.text = model.lowestTemperature()
    highView.text = model.highestTemperature()
  }

  override fun setUpView(rootView: View) = Unit
  override fun hookListeners(rootView: View) = Unit
}
