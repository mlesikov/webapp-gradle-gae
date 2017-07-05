package com.mlesikov

import com.google.appengine.repackaged.com.google.gson.Gson
import spark.Filter
import spark.ResponseTransformer
import spark.Route
import spark.Spark.get
import spark.kotlin.before
import spark.kotlin.halt
import spark.servlet.SparkApplication


/**
 *
 * @author Mihail Lesikov (mlesikov@gmail.com)
 */
class AppBootstrap : SparkApplication {

  override fun init() {
    //scurity filter
    before(Filter { req, res ->
      val authenticated: Boolean = true
      // ... check if authenticated
      if (!authenticated) {
        halt(401, "You are not welcome here")
      }
    })

    //encoding filter
    before(Filter { req, res ->
      res.raw().setCharacterEncoding("UTF-8")
    })

    get("/") { req, res -> "Hello Spark on GAE World" }

    get("/hello") { req, res -> "Hello World" }

    get("/json", "application/json", Route({ req, res -> MyMessage("Hello World") }), JsonTransformer())
  }

}


//  /json
data class MyMessage(val msg: String)

class JsonTransformer : ResponseTransformer {

  private val gson = Gson()

  override fun render(model: Any): String {
    return gson.toJson(model)
  }

}