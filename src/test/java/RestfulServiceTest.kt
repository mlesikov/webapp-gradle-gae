import com.clouway.telcong.apigateway.e2e.TinySparkAppEngineServer
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import util.JsonBuilder
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*

/**
 *
 * @author Mihail Lesikov (mlesikov@gmail.com)
 */

class RestfulServiceTest {
  private var server: TinySparkAppEngineServer? = null

  @Before
  fun init() {
    server = TinySparkAppEngineServer(5678, "com.mlesikov.AppBootstrap")
    server!!.start()
  }

  @After
  fun after() {
    server!!.stop()
  }

  @Test
  fun saveAndGetMsg() {
    val liness = readLines(URL("http://localhost:5678/msg/save"))
    Assert.assertThat<List<String>>(liness, `is`<List<String>>(Arrays.asList(JsonBuilder.aNewJson().add("msg", "Hello World").build())))
    

    val lines = readLines(URL("http://localhost:5678/msg"))
    Assert.assertThat<List<String>>(lines, `is`<List<String>>(Arrays.asList(JsonBuilder.aNewJson().add("msg", "Hello World").build())))
  }

  @Test
  fun getMsg() {

    val lines = readLines(URL("http://localhost:5678/msg"))
    Assert.assertThat<List<String>>(lines, `is`<List<String>>(Arrays.asList(JsonBuilder.aNewJson().add("msg", "Hello World").build())))
  }

  private fun readLines(url: URL): List<String> {
    val lines = ArrayList<String>()

    val `in` = url.openStream()
    BufferedReader(InputStreamReader(`in`, StandardCharsets.UTF_8)).use { r ->
      var line = r.readLine()
      while (line != null) {
        lines.add(line)
        line = r.readLine()
      }
    }
    return lines
  }

}