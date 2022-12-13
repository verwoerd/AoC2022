package day13.part1

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 13/12/2022
 */
fun day13Part1(input: BufferedReader): Any {
  return input.lineSequence().chunked(3) { (a, b) -> a.splitPackets() to b.splitPackets() }
    .mapIndexed { index, (l, r) -> if (comparator.compare(l, r) < 0) index + 1 else 0 }.sum()
}

val comparator: Comparator<JsonArray> = Comparator { l: JsonArray, r: JsonArray ->
  l.zip(r).asSequence().map { (a, b) -> contentComparator.compare(a, b) }.firstOrNull { it != 0 } ?: when {
    l.size > r.size -> 1
    l.size == r.size -> 0
    else -> -1
  }
}

val contentComparator: Comparator<JsonElement> = Comparator<JsonElement> { l: JsonElement, r: JsonElement ->
  when {
    l is JsonPrimitive && r is JsonPrimitive -> l.content.toInt().compareTo(r.content.toInt())
    l is JsonArray && r is JsonArray -> comparator.compare(l, r)
    l is JsonPrimitive && r is JsonArray -> comparator.compare(buildJsonArray { add(l) }, r)
    l is JsonArray && r is JsonPrimitive -> comparator.compare(l, buildJsonArray { add(r) })
    else -> error("Unknown case")
  }
}

fun String.splitPackets(): JsonArray = Json.decodeFromString(this)
