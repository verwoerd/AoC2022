package day08.part1

import toIntValue
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 08/12/2022
 */
fun day08Part1(input: BufferedReader): Any {
  val map = input.lineSequence().map { line ->
      line.map { it.toIntValue() }.toList()
    }.toList()

  var visible = 0
  val yMax = map.size - 1
  val xMax = map.first().size - 1
  map.indices.forEach { y ->
    map.first().indices.forEach { x ->
      if ((y == 0) || (y == yMax) || x == 0 || x == xMax) {
        visible++
      } else {
        val current = map[y][x]
        if ((0 until x).all { map[y][it] < current } || (x + 1..xMax).all { map[y][it] < current } || (0 until y).all { map[it][x] < current } || (y + 1..yMax).all { map[it][x] < current }) {
          visible++
        }
      }
    }
  }
  return visible
}
