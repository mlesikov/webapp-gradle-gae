package util

import com.google.appengine.repackaged.com.google.gson.*

/**
 *
 * @author Mihail Lesikov (mlesikov@gmail.com)
 */

class JsonBuilder(private val target: JsonElement) {

  fun add(property: String, value: String): JsonBuilder {
    target.asJsonObject.addProperty(property, value)
    return this
  }

  fun add(property: String, value: Long?): JsonBuilder {
    target.asJsonObject.addProperty(property, value)
    return this
  }

  fun add(property: String, value: Int?): JsonBuilder {
    target.asJsonObject.addProperty(property, value)
    return this
  }

  fun add(property: String, value: Double?): JsonBuilder {
    target.asJsonObject.addProperty(property, value)
    return this
  }

  fun add(property: String, value: Boolean): JsonBuilder {
    target.asJsonObject.addProperty(property, value)
    return this
  }

  fun add(property: String, value: JsonBuilder): JsonBuilder {
    target.asJsonObject.add(property, value.asJsonElement())
    return this
  }

  fun add(property: String, `object`: Map<String, String>): JsonBuilder {
    val o = JsonObject()
    for (key in `object`.keys) {
      o.addProperty(key, `object`[key])
    }
    target.asJsonObject.add(property, o)
    return this
  }

  fun add(property: String, values: Set<*>): JsonBuilder {
    val array = JsonArray()
    for (each in values) {
      if (each is Long) {
        array.add(JsonPrimitive(each))
      }
      if (each is String) {
        array.add(JsonPrimitive(each))
      }
      if (each is Boolean) {
        array.add(JsonPrimitive(each))
      }

    }
    target.asJsonObject.add(property, array)

    return this
  }

  fun add(property: String, values: List<String>): JsonBuilder {
    val array = JsonArray()
    for (each in values) {
      array.add(JsonPrimitive(each))
    }
    target.asJsonObject.add(property, array)
    return this
  }

  fun addIntegersAsList(property: String, values: List<Int>): JsonBuilder {
    val array = JsonArray()
    for (each in values) {
      array.add(JsonPrimitive(each))
    }
    target.asJsonObject.add(property, array)
    return this
  }

  /**
   * Use static factory methods for adding of JSON objects to an array as it separates
   * the array design from the simple object design.

   * In the future this method is going to be removed.

   * @see JsonBuilder.aNewJsonArray
   */
  @Deprecated("")
  fun withElements(vararg builders: JsonBuilder): JsonBuilder {
    for (each in builders) {
      target.asJsonArray.add(each.target)
    }

    return this
  }

  fun asJsonElement(): JsonElement {
    return target
  }


  fun build(): String {
    return GSON.toJson(target)
  }

  companion object {
    private val GSON = Gson()

    fun aNewJson(): JsonBuilder {
      return JsonBuilder(JsonObject())
    }

    fun aNewJsonArray(): JsonBuilder {
      return JsonBuilder(JsonArray())
    }

    fun aNewJsonArray(vararg builders: JsonBuilder): JsonBuilder {
      val target = JsonArray()
      for (each in builders) {
        target.asJsonArray.add(each.target)
      }
      return JsonBuilder(target)
    }

    fun aNewJsonArray(vararg values: Int): JsonBuilder {
      val target = JsonArray()
      for (each in values) {
        target.asJsonArray.add(each)
      }
      return JsonBuilder(target)
    }

    fun aNewJsonArray(vararg values: String): JsonBuilder {
      val target = JsonArray()
      for (each in values) {
        target.asJsonArray.add(each)
      }
      return JsonBuilder(target)
    }
  }
}
