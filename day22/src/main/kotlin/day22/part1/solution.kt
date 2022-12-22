package day22.part1

import Coordinate
import FourDirections
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 22/12/2022
 */
fun day22Part1(input: BufferedReader): Any {
  val lines = input.readLines()
  val map = lines.takeWhile { it.isNotBlank() }
      .mapIndexed { y, line ->
        line.mapIndexedNotNull() { x, c ->
          when (c) {
            '.', '#' -> Coordinate(x + 1, y + 1) to c
            else -> null
          }
        }
      }.flatten().toMap()
  val commands = lines.last().fold(emptyList<Any>() to "") { (list, last), c ->
    when (c) {
      'L' -> (list + listOf(last.toInt(), FourDirections.LEFT)) to ""
      'R' -> (list + listOf(last.toInt(), FourDirections.RIGHT)) to ""
      else -> list to last + c
    }
  }.let { (list, last) -> list + listOf(last.toInt()) }
//  println(commands)
  val startCoordinate = map.keys.filter { it.y == 1 }.minBy { it.x }
  val startDirection = FourDirections.RIGHT
  val mapCache = mutableMapOf<Pair<Coordinate, FourDirections>, Coordinate>()
  val (endPoint, endDirection) = commands.fold(startCoordinate to startDirection) { (location, direction), option ->
//    println("$location, $direction")
    when (option) {
      is FourDirections -> location to if (option == FourDirections.LEFT) direction.turnLeft() else direction.turnRight()
      is Int -> {
        var current = location
        repeat(option) {
          current = mapCache.computeIfAbsent(current to direction) { (l, d) ->
            var next = l + when (d) {
              FourDirections.LEFT, FourDirections.RIGHT -> d.direction
              FourDirections.UP -> FourDirections.DOWN.direction
              FourDirections.DOWN -> FourDirections.UP.direction
            }
            if (next !in map.keys) {
              next = when (d) {
                FourDirections.LEFT -> map.keys.filter { it.y == l.y }.maxBy { it.x }
                FourDirections.RIGHT -> map.keys.filter { it.y == l.y }.minBy { it.x }
                FourDirections.DOWN -> map.keys.filter { it.x == l.x }.minBy { it.y }
                FourDirections.UP -> map.keys.filter { it.x == l.x }.maxBy { it.y }
              }
            }
            when (map[next]!!) {
              '#' -> l
              '.' -> next
              else -> error("Unknown location type")
            }
          }
        }
        current to direction
      }

      else -> error("found a weird command $option")
    }
  }
//  println("$endPoint, $endDirection")
  return 1000 * endPoint.y + 4 * endPoint.x + when (endDirection) {
    FourDirections.LEFT -> 2
    FourDirections.RIGHT -> 0
    FourDirections.DOWN -> 1
    FourDirections.UP -> 3
  }
}
