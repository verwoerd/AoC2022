package day22.part2

import Coordinate
import FourDirections
import FourDirections.*
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 22/12/2022
 */
fun day22Part2(input: BufferedReader, blockSize: Int = 50): Any {
  val lines = input.readLines()
  val map = lines.dropLast(2)
      .mapIndexed { y, line ->
        line.mapIndexedNotNull() { x, c ->
          when (c) {
            '.', '#' -> Coordinate(x, y) to c
            else -> null
          }
        }
      }.flatten().toMap()
  val commands = lines.last().fold(emptyList<Any>() to "") { (list, last), c ->
    when (c) {
      'L' -> (list + listOf(last.toInt(), LEFT)) to ""
      'R' -> (list + listOf(last.toInt(), RIGHT)) to ""
      else -> list to last + c
    }
  }.let { (list, last) -> list + listOf(last.toInt()) }


  val startCoordinate = map.keys.filter { it.y == 1 }.minBy { it.x }
  val planes = (0..3).flatMap { y ->
    (0..3).mapNotNull { x ->
      when (val start = Coordinate(x * blockSize + 1, y * blockSize + 1)) {
        in map.keys -> (start.x until start.x + blockSize) to (start.y until start.y + blockSize)
        else -> null
      }
    }
  }

  val startDirection = RIGHT
  val mapCache = mutableMapOf<Pair<Coordinate, FourDirections>, Pair<Coordinate, FourDirections>>()
  val (endPoint, endDirection) = commands.fold(startCoordinate to startDirection) { (location, direction), option ->
    when (option) {
      is FourDirections -> location to if (option == LEFT) direction.turnLeft() else direction.turnRight()
      is Int -> {
        var current = location
        var currentDirection = direction
        repeat(option) {
          val (nc, nd) = mapCache.computeIfAbsent(current to direction) { (l, d) ->
            var next = l + when (d) {
              LEFT, RIGHT -> d.direction
              UP -> DOWN.direction
              DOWN -> UP.direction
            }
            var nextDirection = d
            if (next !in map.keys) {
              println("considering $next $nextDirection $l")
              val (nc, nd) = when (d) {
                LEFT -> when (l.x) {
                  0 -> when (l.y) {
                    in (100..149) -> Coordinate(50, 49 - (l.y - 100)) to RIGHT // 4 -> 1
                    in 150..199 -> Coordinate(50 + (l.y - 150), 0) to DOWN // 6 -> 1
                    else -> error("out of bounds")
                  }

                  50 -> when (l.y) {
                    in 0..49 -> Coordinate(0, 100 + (49 - l.y)) to RIGHT // 1 -> 4
                    in 50..99 -> Coordinate(l.y - 50, 100) to DOWN // 3 -> 4
                    else -> error("out of bounds")
                  }

                  else -> error("out of bounds")
                }

                RIGHT -> when (l.x) {
                  49 -> Coordinate(50 + (l.y - 150), 149) to UP// 6->5
                  99 -> when (l.y) {
                    in 50..99 -> Coordinate(l.y - 50 + 100, 49) to UP // 3 -> 2
                    in 100..149 -> Coordinate(149, 49 - (l.y - 100)) to LEFT //  5 -> 2
                    else -> error("out of bounds")
                  }

                  149 -> Coordinate(99, 149 - l.y) to LEFT // 2 -> 5
                  else -> error("out of bounds")
                }

                DOWN -> when (l.y) {
                  49 -> Coordinate(99, 50 + (l.x - 100)) to RIGHT//2-> 3
                  149 -> Coordinate(49, 150 + (l.x - 50)) to RIGHT // 5 -> 6
                  199 -> Coordinate(100 + l.x, 0) to DOWN// 6 -> 2
                  else -> error("out of bounds")
                }

                UP -> when (l.y) {
                  0 -> when (l.x) {
                    in 50..99 -> Coordinate(0, 150 + (l.x - 50)) to RIGHT//1 -> 6
                    in 100..149 -> Coordinate(l.x - 100, 199) to UP // 2 -> 6
                    else -> error("out of bounds")
                  }

                  100 -> Coordinate(50, 50 + l.x) to RIGHT//4 -> 3
                  else -> error("out of bounds")
                }
              }
              nextDirection = nd
              next = nc
//              println("Calculating wrapping $current $currentDirection to $next $nextDirection")
            }
            when (map[next]!!) {
              '#' -> l to d
              '.' -> next to nextDirection
              else -> error("Unknown location type")
            }
          }
          current = nc
          currentDirection = nd
        }
        current to currentDirection
      }

      else -> error("found a weird command $option")
    }
  }
  println("$endPoint, $endDirection")
  return 1000 * endPoint.y + 4 * endPoint.x + when (endDirection) {
    LEFT -> 2
    RIGHT -> 0
    DOWN -> 1
    UP -> 3
  }
}
