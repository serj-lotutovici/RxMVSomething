package com.serjltt.devfest.weather.data.network

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


class JsonTestRule : TestRule {
  val moshi = JsonModule().provideMoshi()

  override fun apply(base: Statement, description: Description) =
      object : Statement() {
        override fun evaluate() {
          base.evaluate()
        }
      }
}