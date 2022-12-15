package day15.part2

import Coordinate
import day15.part1.regex
import manhattanDistance
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 15/12/2022
 */
fun day15Part2(input: BufferedReader, maxCoordinate: Int = 4000000): Any {
  val ranges = input.lineSequence()
    .flatMap { line ->
      val (lx, ly, rx, ry) = regex.matchEntire(line)!!.destructured
      val sensor = Coordinate(lx.toInt(), ly.toInt())
      val beacon = Coordinate(rx.toInt(), ry.toInt())
      val distance = manhattanDistance(sensor, beacon)
      (0 until distance).flatMap { curent ->
        val diff = distance - curent
        val xRange = ((sensor.x - diff).coerceAtLeast(0)..(sensor.x + diff).coerceAtMost(maxCoordinate))
        if (sensor.y + curent > maxCoordinate && sensor.y - curent < 0) {
          emptyList()
        } else if (curent == 0) {
          listOf(sensor.y to xRange)
        } else {
          listOfNotNull((sensor.y + curent to xRange).takeIf { it.first <= maxCoordinate },
            (sensor.y - curent to xRange).takeIf { it.first >= 0 })
        }
      }
    }.filter { (sensor) -> sensor in (0..maxCoordinate) }
    .groupBy({ it.first }) { it.second }

  ranges.entries.forEach { (y, range) ->
    range.sortedBy { it.first }
      .fold(0..0) { acc, intRange ->
        when {
          intRange.last in acc -> acc
          intRange.first in acc -> acc.first..intRange.last
          intRange.first - 1 in acc -> acc.first..intRange.last
          else -> return 4000000L * (acc.last + 1) + y
        }
      }
  }
  error(" no result")
}
