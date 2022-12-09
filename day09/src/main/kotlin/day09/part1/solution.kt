package day09.part1

import FourDirections
import origin
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 09/12/2022
 */
fun day09Part1(input: BufferedReader): Any =
  input.readSimulation().fold(Triple(origin, origin, mutableListOf(origin))) { (h, t, path), (direction, times) ->
    var currentH = h
    var currentT = t
    repeat(times) {
      currentH += direction.direction
      val diff = currentH - currentT
      if (diff.x !in (-1..1) || diff.y !in (-1..1)) {
        currentT += diff.normalize()
        path.add(currentT)
      }
    }
    Triple(currentH, currentT, path)
  }.third.distinct().size

fun BufferedReader.readSimulation() = lineSequence().map { line ->
  line.split(" ").let { (l, r) ->
    when (l) {
      "U" -> FourDirections.UP
      "D" -> FourDirections.DOWN
      "L" -> FourDirections.LEFT
      "R" -> FourDirections.RIGHT
      else -> error("Invalid input")
    } to r.toInt()
  }
}
