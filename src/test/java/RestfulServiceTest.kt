import com.clouway.telcong.apigateway.e2e.TinySparkServer
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
  private var server: TinySparkServer? = null

  @Before
  fun init() {
    server = TinySparkServer(5678, "com.mlesikov.AppBootstrap")
    server!!.start()
  }

  @After
  fun after() {
    server!!.stop()
  }

  @Test
  fun getJson() {
    val lines = readLines(URL("http://localhost:5678/json"))
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