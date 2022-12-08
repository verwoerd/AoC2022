package day08.part2

import toIntValue
import java.io.BufferedReader
import java.lang.Integer.max

/**
 * @author verwoerd
 * @since 08/12/2022
 */
fun day08Part2(input: BufferedReader): Any {
  val map = input.lineSequence().map { line ->
      line.map { it.toIntValue() }.toList()
    }.toList()

  var visible = 0
  val yMax = map.size - 1
  val xMax = map.first().size - 1
  map.indices.forEach { y ->
    map.first().indices.forEach { x ->
      val current = map[y][x]
      visible = max(visible,
        (x - 1 downTo 0).takeWhile { map[y][it] < current }.count()
          .let { it + if (x - it > 0) 1 else 0 } * (x + 1..xMax).takeWhile { map[y][it] < current }.count()
          .let { it + if (x + it < xMax) 1 else 0 } * (y - 1 downTo 0).takeWhile { map[it][x] < current }.count()
          .let { it + if (y - it > 0) 1 else 0 } * (y + 1..yMax).takeWhile { map[it][x] < current }.count()
          .let { it + if (y + it < yMax) 1 else 0 })
    }
  }
  return visible
}
