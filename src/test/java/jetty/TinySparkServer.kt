package com.clouway.telcong.apigateway.e2e


import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext
import spark.Spark
import spark.servlet.SparkFilter
import java.util.*
import javax.servlet.DispatcherType


/**
 * TinyServerApp is an HTTP server application with start/stop workflow that serves Spark applications.
 *
 *
 * It's designed for testing purposes and should be used only for that.
 *
 * @author Mihail Lesikov (mihail.leiskov@clouway.com)
 */
class TinySparkServer(port: Int = Spark.port(), val applicationClass: String = "my.package.MySparkApplication") {

  private val server: Server = Server(port)

  fun start() {

    val webapp = WebAppContext()
    webapp.contextPath = "/"
    webapp.war = "/"
    val holder = webapp.addFilter(SparkFilter::class.java, "/*", EnumSet.of(DispatcherType.REQUEST))
    holder.setInitParameter("applicationClass", applicationClass)
    server.handler = webapp

    try {
      server.start()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun stop() {
    try {
      server.stop()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }
}

