package day14.part1

import Coordinate
import FourDirections.DOWN
import FourDirections.LEFT
import FourDirections.RIGHT
import xRange
import yRange
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 14/12/2022
 */
val source = Coordinate(500, 0)
val impassible = setOf('#', 'o')
fun day14Part1(input: BufferedReader): Any {
  val map = input.readInput()
  val xRange = map.xRange().let { (min, max) -> min - 1..max + 1 }
  val yRange = map.yRange().let { (min, max) -> min - 1..max + 1 }
  val count = map.simulateSand(xRange, yRange) { map.getValue(it) == 'o' }
//  map.printMap() { "${it ?: '.'}" }
  return count
}

fun BufferedReader.readInput(): MutableMap<Coordinate, Char> {
  val map = mutableMapOf(source to '+').withDefault { '.' }
  lineSequence()
    .map { line ->
      line.split(" -> ").map { it.split(",").map(String::toInt).let { (x, y) -> Coordinate(x, y) } }
    }.forEach { line ->
      line.windowed(2).forEach { (l, r) ->
        when {
          l.x != r.x && l.y != r.y -> error("diagonal line found")
          l.x < r.x -> (l.x..r.x).forEach { x -> map[Coordinate(x, l.y)] = '#' }
          l.x > r.x -> (r.x..l.x).forEach { x -> map[Coordinate(x, l.y)] = '#' }
          l.y < r.y -> (l.y..r.y).forEach { y -> map[Coordinate(l.x, y)] = '#' }
          l.y > r.y -> (r.y..l.y).forEach { y -> map[Coordinate(l.x, y)] = '#' }
          else -> error("unknown input $l -> $r")
        }
      }
    }
  return map
}

fun MutableMap<Coordinate, Char>.simulateSand(
  xRange: IntRange,
  yRange: IntRange,
  stopCondition: (Coordinate) -> Boolean
) = sequence {
  while (true) {
    yield(source)
  }
}.takeWhile {
  var current = it
  while (current.x in xRange && current.y in yRange) {
    when {
      getValue(current - DOWN.direction - LEFT.direction) in impassible
          && getValue(current - DOWN.direction) in impassible
          && getValue(current - DOWN.direction - RIGHT.direction) in impassible -> {
        this[current] = 'o'
        break
      }

      getValue(current - DOWN.direction) == '.' -> current -= DOWN.direction
      getValue(current - DOWN.direction + LEFT.direction) == '.' -> current =
        current - DOWN.direction + LEFT.direction

      getValue(current - DOWN.direction + RIGHT.direction) == '.' -> current =
        current - DOWN.direction + RIGHT.direction

      else -> error("I got a weird case ")
    }
  }
  stopCondition(current)
}.count()
