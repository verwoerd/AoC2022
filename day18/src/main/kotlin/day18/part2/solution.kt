package day18.part2

import TripleCoordinate
import adjacentTripleCoordinates
import linkedListOf
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 18/12/2022
 */
fun day18Part2(input: BufferedReader): Any {
  val drops = input.lineSequence()
    .map { line -> line.split(",").map { it.toInt() }.let { (x, y, z) -> TripleCoordinate(x, y, z) } }
    .toMutableSet()
  val xRange = (drops.minOf { it.x } - 1..drops.maxOf { it.x } + 1)
  val yRange = (drops.minOf { it.y } - 1..drops.maxOf { it.y } + 1)
  val zRange = (drops.minOf { it.z } - 1..drops.maxOf { it.z } + 1)
  val start = TripleCoordinate(xRange.first, yRange.first, zRange.first)
  var count = 0
  val seen = mutableSetOf(start)
  val queue = linkedListOf(start)
  while (queue.isNotEmpty()) {
    val current = queue.removeFirst()
    adjacentTripleCoordinates(current)
      .onEach { if (it in drops) count++ }
      .filter { it !in drops }
      .filter { seen.add(it) }
      .filter { it.x in xRange && it.y in yRange && it.z in zRange }
      .toCollection(queue)
  }
  return count
}
