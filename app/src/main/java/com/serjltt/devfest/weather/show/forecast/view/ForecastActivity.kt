package com.serjltt.devfest.weather.show.forecast.view

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.Unbinder
import com.jakewharton.rxbinding2.view.clicks
import com.pedrogomez.renderers.ListAdapteeCollection
import com.pedrogomez.renderers.RVRendererAdapter
import com.pedrogomez.renderers.Renderer
import com.pedrogomez.renderers.RendererBuilder
import com.serjltt.devfest.weather.R
import com.serjltt.devfest.weather.di.Injector
import com.serjltt.devfest.weather.di.InjectorActivity
import com.serjltt.devfest.weather.mvp.Presenter
import com.serjltt.devfest.weather.show.forecast.ForecastData
import com.serjltt.devfest.weather.show.forecast.ForecastUiModel
import com.serjltt.devfest.weather.show.forecast.ForecastModule
import com.serjltt.devfest.weather.show.forecast.ForecastView
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ForecastActivity : InjectorActivity(), ForecastView {
  @BindView(R.id.forecast_list) lateinit var recyclerView: RecyclerView
  @BindView(R.id.forecast_progress) lateinit var progressView: ProgressBar
  @BindView(R.id.forecast_error) lateinit var errorView: TextView
  @BindView(R.id.forecast_toolbar) lateinit var toolbarView: Toolbar
  @BindView(R.id.forecast_fab) lateinit var selectCityView: FloatingActionButton

  @Inject lateinit var presenter: Presenter<ForecastView>

  lateinit var adapter: RVRendererAdapter<ForecastData>
  lateinit var disposable: Disposable
  lateinit var unbinder: Unbinder

  override fun onInject(injector: Injector) {
    injector.forecastUiComponent(ForecastModule(this)).inject(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_forecast)
    unbinder = ForecastActivity_ViewBinding(this)

    setSupportActionBar(toolbarView)

    val rendererBuilder = RendererBuilder<ForecastData>(ModelRenderer())
    adapter = RVRendererAdapter<ForecastData>(rendererBuilder,
        ListAdapteeCollection<ForecastData>())
    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.adapter = adapter

    disposable = presenter.bind(this)
  }

  override fun onDestroy() {
    disposable.dispose()
    unbinder.unbind()
    super.onDestroy()
  }

  override fun selectCity(): Observable<Unit> = selectCityView.clicks()

  override fun updateView(model: ForecastUiModel) {
    when (model) {
      is ForecastUiModel.Progress -> showLoading()
      is ForecastUiModel.Error -> {
        hideLoading()
        showError(model.message)
      }
      is ForecastUiModel.Success -> {
        hideLoading()
        showForecast(model)
      }
    }
  }

  private fun showLoading() {
    progressView.visibility = View.VISIBLE
    errorView.visibility = View.GONE
    recyclerView.visibility = View.GONE
  }

  private fun hideLoading() {
    progressView.visibility = View.GONE
  }

  private fun showError(error: String?) {
    errorView.visibility = View.VISIBLE
    errorView.text = error
  }

  private fun showForecast(model: ForecastUiModel.Success) {
    recyclerView.visibility = View.VISIBLE

    supportActionBar!!.title = getString(R.string.screen_name, model.cityName)

    adapter.clear()
    adapter.addAll(model.data)
    adapter.notifyDataSetChanged()
  }
}

private class ModelRenderer : Renderer<ForecastData>() {
  @BindView(R.id.item_date) lateinit var dateView: TextView
  @BindView(R.id.item_low) lateinit var lowView: TextView
  @BindView(R.id.item_high) lateinit var highView: TextView

  override fun inflate(inflater: LayoutInflater, parent: ViewGroup): View =
      inflater.inflate(R.layout.list_item_forecast, parent, false)
          .also { view -> ModelRenderer_ViewBinding(this@ModelRenderer, view) }

  override fun render() {
    val model = content
    dateView.text = model.date
    lowView.text = model.low
    highView.text = model.high
  }

  override fun setUpView(rootView: View) = Unit
  override fun hookListeners(rootView: View) = Unit
}
