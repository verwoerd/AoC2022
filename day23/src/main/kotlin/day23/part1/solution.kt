package day23.part1

import Coordinate
import FourDirections
import adjacentCircularCoordinates
import linkedListOf
import origin
import rotate
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 23/12/2022
 */
fun day23Part1(input: BufferedReader): Any {
  val raw = input.readLines()
  var elfes = raw.flatMapIndexed { y, line ->
    line.mapIndexedNotNull() { x, c ->
      when (c) {
        '#' -> Coordinate(x, raw.size - y)
        else -> null
      }
    }
  }.toSet()
  val nextMap = mapOf(
      FourDirections.UP to listOf(Coordinate(origin.x + 1, origin.y + 1), Coordinate(origin.x, origin.y + 1), Coordinate(origin.x - 1, origin.y + 1)),//N
      FourDirections.DOWN to listOf(Coordinate(origin.x + 1, origin.y - 1), Coordinate(origin.x, origin.y - 1), Coordinate(origin.x - 1, origin.y - 1)),//S
      FourDirections.RIGHT to listOf(Coordinate(origin.x + 1, origin.y + 1), Coordinate(origin.x + 1, origin.y), Coordinate(origin.x + 1, origin.y - 1)),//W
      FourDirections.LEFT to listOf(Coordinate(origin.x - 1, origin.y + 1), Coordinate(origin.x - 1, origin.y), Coordinate(origin.x - 1, origin.y - 1)),//E)

  )
  val order = linkedListOf(FourDirections.UP, FourDirections.DOWN, FourDirections.LEFT, FourDirections.RIGHT)
  repeat(10) { _ ->
    val next = mutableSetOf<Coordinate>()
    val moves = elfes
        .filter { c -> adjacentCircularCoordinates(c).any { it in elfes }.also { r -> if (!r) next.add(c) } }
        .map { coo -> coo to order.firstOrNull { direction -> nextMap[direction]!!.all { coo + it !in elfes } } }
        .map { (c, d) -> c to d?.direction?.let { it + c } }
        .filter { (a, b) -> (b != null).also { if (!it) next.add(a) } }
    moves.filter { (c, n) -> (moves.count { it.second == n } == 1).also { if (!it) next.add(c) } }
        .mapNotNull { it.second }.toCollection(next)
    elfes = next
    order.rotate(1)
  }
  val yRange = elfes.minByOrNull { it.y }!!.y..elfes.maxByOrNull { it.y }!!.y
  val xRange = elfes.minByOrNull { it.x }!!.x..elfes.maxByOrNull { it.x }!!.x
  return yRange.sumOf { y ->
    xRange.count { x -> Coordinate(x, y) !in elfes }
  }
}

fun Set<Coordinate>.printMap(): String {
  val yRange = maxByOrNull { it.y }!!.y downTo minByOrNull { it.y }!!.y
  val xRange = minByOrNull { it.x }!!.x..maxByOrNull { it.x }!!.x
  return yRange.joinToString("\n") { y ->
    xRange.joinToString("") { x -> if (Coordinate(x, y) in this) "#" else "." }
  }
}
