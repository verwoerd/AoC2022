package day12.part2

import day12.part1.dijkstra
import day12.part1.readMap
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 12/12/2022
 */
fun day12Part2(input: BufferedReader): Any {
  val (_, end, map) = input.readMap()
  return map.dijkstra(end, { _, it -> it == 0 }) { it, height -> it in (height - 1..26) }
}
