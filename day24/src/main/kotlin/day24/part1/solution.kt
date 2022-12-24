package day24.part1

import Coordinate
import FourDirections
import adjacentCoordinates
import manhattanDistance
import priorityQueueOf
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 24/12/2022
 */
fun day24Part1(input: BufferedReader): Any {
  val coordinates = input.lineSequence()
    .flatMapIndexed { y, line ->
      line.mapIndexed { x, c -> Coordinate(x, y) to c }
    }.toList()
  val yRange = 1 until coordinates.maxOf { it.first.y }
  val xRange = 1 until coordinates.maxOf { it.first.x }
  val start = coordinates.filter { it.first.y == 0 }.first { it.second == '.' }.first
  val end =
    coordinates.filter { it.first.y == yRange.last + 1 }
      .first { it.second == '.' }.first + FourDirections.DOWN.direction
  val blizzardLocations = coordinates.filter { it.second != '.' }.filter { it.second != '#' }
    .map {
      it.first to when (it.second) {
        '>' -> FourDirections.RIGHT
        '<' -> FourDirections.LEFT
        '^' -> FourDirections.DOWN
        'v' -> FourDirections.UP
        else -> error("Unknown direction $it")
      }
    }.toSet()
  val blizzardMap = mutableMapOf(0 to blizzardLocations)

  val seen = mutableSetOf(State(0, start))
  val queue = priorityQueueOf<State>(
    { a, b ->
      when (val c = a.minutes.compareTo(b.minutes)) {
        0 -> manhattanDistance(a.location, end).compareTo(manhattanDistance(b.location, end))
        else -> c
      }
    },
    State(0, start)
  )
  while (queue.isNotEmpty()) {
    val (minutes, location) = queue.remove()
    println("[$minutes] $location")
    if (location == end) {
      return minutes + 1
    }
    val nextBlizzard = blizzardMap.computeIfAbsent(minutes + 1) {
      blizzardMap[minutes]!!.map { (current, direction) ->
        val next = current + direction.direction
        if (next.x in xRange && next.y in yRange) {
          next
        } else {
          when (direction) {
            FourDirections.UP -> Coordinate(next.x, 1)
            FourDirections.DOWN -> Coordinate(next.x, yRange.last)
            FourDirections.LEFT -> Coordinate(xRange.last, next.y)
            FourDirections.RIGHT -> Coordinate(1, next.y)
          }
        } to direction
      }.toSet()
    }
    val nextBl = nextBlizzard.map { it.first }
    adjacentCoordinates(location).filter { it !in nextBl }
      .filter { it.x in xRange && it.y in yRange }
      .map { State(minutes + 1, it) }
      .filter { seen.add(it) }
      .toCollection(queue)
    if (location !in nextBl) {
      State(minutes + 1, location).takeIf { seen.add(it) }?.let { queue.add(it) }
    }
  }
  error("no path found")
}

data class State(
  val minutes: Int,
  val location: Coordinate
)
