package day24.part2

import Coordinate
import FourDirections
import adjacentCoordinates
import day24.part1.State
import manhattanDistance
import priorityQueueOf
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 24/12/2022
 */
fun day24Part2(input: BufferedReader): Any {
  val coordinates = input.lineSequence()
    .flatMapIndexed { y, line ->
      line.mapIndexed { x, c -> Coordinate(x, y) to c }
    }.toList()
  val yRange = 1 until coordinates.maxOf { it.first.y }
  val xRange = 1 until coordinates.maxOf { it.first.x }
  val start = coordinates.filter { it.first.y == 0 }.first { it.second == '.' }.first
  val startTarget = start + FourDirections.UP.direction
  val end =
    coordinates.filter { it.first.y == yRange.last + 1 }
      .first { it.second == '.' }.first
  val endTarget = end + FourDirections.DOWN.direction
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
  var heenweg = 0
  while (queue.isNotEmpty()) {
    val (minutes, location) = queue.remove()
    println("[$minutes] $location")
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
    if (location == endTarget) {
      heenweg = minutes + 1
      break
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
  println("Heenweg found in $heenweg")
  val seen2 = mutableSetOf(State(heenweg, end))
  val queue2 = priorityQueueOf<State>(
    { a, b ->
      when (val c = a.minutes.compareTo(b.minutes)) {
        0 -> manhattanDistance(a.location, startTarget).compareTo(manhattanDistance(b.location, startTarget))
        else -> c
      }
    },
    State(heenweg, end)
  )
  var terugweg = 0
  while (queue2.isNotEmpty()) {
    val (minutes, location) = queue2.remove()
    println("[$minutes] $location")
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
    if (location == startTarget) {
      terugweg = minutes + 1
      break
    }
    val nextBl = nextBlizzard.map { it.first }
    adjacentCoordinates(location).filter { it !in nextBl }
      .filter { it.x in xRange && it.y in yRange }
      .map { State(minutes + 1, it) }
      .filter { seen2.add(it) }
      .toCollection(queue2)
    if (location !in nextBl) {
      State(minutes + 1, location).takeIf { seen2.add(it) }?.let { queue2.add(it) }
    }
  }
  if (terugweg == 0) error("NO way found")
  println("Terugweg found in $terugweg")
  val seen3 = mutableSetOf(State(terugweg, start))
  val queue3 = priorityQueueOf<State>(
    { a, b ->
      when (val c = a.minutes.compareTo(b.minutes)) {
        0 -> manhattanDistance(a.location, end).compareTo(manhattanDistance(b.location, end))
        else -> c
      }
    },
    State(terugweg, start)
  )
  while (queue3.isNotEmpty()) {
    val (minutes, location) = queue3.remove()
    println("[$minutes] $location")
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
    if (location == endTarget) {
      return minutes + 1
      break
    }
    val nextBl = nextBlizzard.map { it.first }
    adjacentCoordinates(location).filter { it !in nextBl }
      .filter { it.x in xRange && it.y in yRange }
      .map { State(minutes + 1, it) }
      .filter { seen3.add(it) }
      .toCollection(queue3)
    if (location !in nextBl) {
      State(minutes + 1, location).takeIf { seen3.add(it) }?.let { queue3.add(it) }
    }
  }
  error("no path found")
}
