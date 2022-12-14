package day14.part2

import Coordinate
import day14.part1.readInput
import day14.part1.simulateSand
import day14.part1.source
import xRange
import yRange
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 14/12/2022
 */
fun day14Part2(input: BufferedReader): Any {
  val map = input.readInput()
  val xRange = map.xRange().let { (min, max) -> min - 1 - (max - min) * 2..max + 1 + (max - min) * 2 + 1 }
  map.yRange().second.let { xRange.forEach { x -> map[Coordinate(x, it + 2)] = '#' } }
  val yRange = map.yRange().let { (min, max) -> min - 1..max }
  val count = map.simulateSand(xRange, yRange) { it != source }
//  map.printMap { "${it ?: '.'}" }
  return count + 1
}
