package com.mlesikov

import com.google.appengine.api.datastore.DatastoreServiceFactory
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.KeyFactory
import com.google.appengine.repackaged.com.google.gson.Gson
import com.google.common.base.Throwables
import spark.Filter
import spark.ResponseTransformer
import spark.Route
import spark.Spark.get
import spark.kotlin.*
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

    get("/json2", "application/json", Route({ req, res ->
      val message: MyMessage = JsonTransformer().from("{msg: \"parsing test - Hello World\"}", MyMessage::class.java)
      return@Route message
    }), JsonTransformer())

    put("/messages/:id", DEFAULT_ACCEPT, { request.params("id") })

    post("/messages/:id", DEFAULT_ACCEPT, {
      val m: MyMessage = JsonTransformer().from(request.body(), MyMessage::class.java)
      MessageRepo().store(m)
    })


    //GAE tests
    get("/msg", "application/json", Route({ req, res ->

      try {
        val dss = DatastoreServiceFactory.getDatastoreService()
        dss.get(KeyFactory.createKey("Message", 123L))
        println(" ------------- entity found")
      } catch (e: Exception) {
        println(Throwables.getStackTraceAsString(e))
      }

      return@Route MyMessage("Hello World")

    }), JsonTransformer())

    //GAE tests
    get("/msg/save", "application/json", Route({ req, res ->

      try {
        val dss = DatastoreServiceFactory.getDatastoreService()
        dss.put(Entity("Message", 123L))

      } catch (e: Exception) {
        println(Throwables.getStackTraceAsString(e))
      }

      return@Route MyMessage("Hello World")

    }), JsonTransformer())

  }

}


class MessageRepo {
  fun store(m: MyMessage) {
  }
}


data class MyMessage(val msg: String)


//  json
class JsonTransformer : ResponseTransformer {

  private val gson = Gson()

  override fun render(model: Any): String {
    return gson.toJson(model)
  }

  fun <T> from(json: String?, clazz: Class<T>): T {
    return gson.fromJson<T>(json, clazz)
  }
}