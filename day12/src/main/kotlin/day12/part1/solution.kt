package day12.part1

import Coordinate
import adjacentCoordinates
import priorityQueueOf
import xRange
import yRange
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 12/12/2022
 */
fun day12Part1(input: BufferedReader): Any {
  val (start, end, map) = input.readMap()
  return map.dijkstra(start, { it, _ -> it == end }) { it, height -> it in (0..height + 1) }
}

fun Map<Coordinate, Int>.dijkstra(
  start: Coordinate, endReached: (Coordinate, Int) -> Boolean, adjacentFilter: (Int, Int) -> Boolean
): Int {
  val xRange = xRange().let { it.first..it.second }
  val yRange = yRange().let { it.first..it.second }
  val queue = priorityQueueOf(compareBy { it.first }, 0 to start)
  val seen = mutableSetOf(start)
  while (queue.isNotEmpty()) {
    val (size, location) = queue.remove()
    val height = get(location)!!
    if (endReached(location, height)) {
      return size
    }
    adjacentCoordinates(location).filter { it.x in xRange && it.y in yRange }
      .filter { adjacentFilter(get(it)!!, height) }.filter { seen.add(it) }.map { size + 1 to it }.toCollection(queue)
  }
  error("no route found")
}

fun BufferedReader.readMap(): Triple<Coordinate, Coordinate, Map<Coordinate, Int>> {
  lateinit var start: Coordinate
  lateinit var end: Coordinate
  val map = lineSequence().flatMapIndexed { y, line ->
    line.mapIndexed { x, c ->
      when (c) {
        'S' -> {
          start = Coordinate(x, y)
          start to 0
        }

        'E' -> {
          end = Coordinate(x, y)
          end to 'z' - 'a'
        }

        else -> Coordinate(x, y) to c - 'a'
      }
    }
  }.toMap()
  return Triple(start, end, map)
}
