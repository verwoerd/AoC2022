package day13.part2

import day13.part1.comparator
import day13.part1.splitPackets
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 13/12/2022
 */

val SIX = buildJsonArray { add(buildJsonArray { add(JsonPrimitive("6")) }) }
val TWO = buildJsonArray { add(buildJsonArray { add(JsonPrimitive("2")) }) }
fun day13Part2(input: BufferedReader): Any {
  val data = input.lineSequence().chunked(3) { (a, b) ->
    listOf(a.splitPackets(), b.splitPackets())
  }.flatten().toList() + listOf(TWO, SIX)
  return data.sortedWith(comparator).foldIndexed(1) { index, acc, jsonArray ->
    when (jsonArray) {
      TWO, SIX -> acc * (index + 1)
      else -> acc
    }
  }
}
