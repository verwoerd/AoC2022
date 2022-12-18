package day18.part1

import TripleCoordinate
import adjacentTripleCoordinates
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 18/12/2022
 */
fun day18Part1(input: BufferedReader): Any {
  val drops = input.lineSequence()
    .map { line -> line.split(",").map { it.toInt() }.let { (x, y, z) -> TripleCoordinate(x, y, z) } }
    .toSet()

  return drops.sumOf { current ->
    adjacentTripleCoordinates(current).filter { it !in drops }.count()
  }
}
