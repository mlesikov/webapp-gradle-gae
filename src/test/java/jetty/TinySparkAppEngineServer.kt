package com.clouway.telcong.apigateway.e2e


import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig
import com.google.appengine.tools.development.testing.LocalServiceTestHelper
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext
import spark.Spark
import spark.servlet.SparkFilter
import java.io.File
import java.util.*
import javax.servlet.*


/**
 * TinySparkAppEngineServer is an HTTP server application with start/stop workflow that serves Spark applications.
 * Uses [LocalServiceTestHelper] to initialize the AppEngine services in development mode
 *
 * !!! DEVELOPMENT only
 *
 * It's designed for testing purposes and should be used only for that.
 *
 * @author Mihail Lesikov (mihail.leiskov@clouway.com)
 */
class TinySparkAppEngineServer(port: Int = Spark.port(), val applicationClass: String = "my.package.MySparkApplication") {

  private val server: Server = Server(port)

  fun start() {

    val webapp = WebAppContext()
    webapp.contextPath = "/"
    webapp.war = "/"
    webapp.addFilter(AppEngineInitializer::class.java, "/*", EnumSet.of(DispatcherType.REQUEST))
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

class AppEngineInitializer : Filter {
  //the datastore should be available for the whole server lifetime, so
  // we set up specific location, that will be cleared on destroy
  val localDatastoreServiceTestConfig = LocalDatastoreServiceTestConfig().setApplyAllHighRepJobPolicy()
          .setNoStorage(false)
          .setBackingStoreLocation("tmp/local_db.bin")
  private val appEngine = LocalServiceTestHelper(
          localDatastoreServiceTestConfig
  )

  override fun init(filterConfig: FilterConfig?) {

  }

  override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
    appEngine.setUp()

    chain!!.doFilter(request, response)

    appEngine.tearDown()
  }

  override fun destroy() {

    val file = File(localDatastoreServiceTestConfig.backingStoreLocation)
    if (file.delete()) {
      println(file.name + " is deleted!")
    } else {
      println("Delete operation is failed.")
    }
  }
}