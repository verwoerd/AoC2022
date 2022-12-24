package day24.part2

import FourDirections
import day24.part1.dfs
import day24.part1.readInput
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 24/12/2022
 */
fun day24Part2(input: BufferedReader): Any {
  val (start, end) = input.readInput()
  val forward = dfs(start, end + FourDirections.DOWN.direction)
  val backward = dfs(end, start + FourDirections.UP.direction, forward)
  return dfs(start, end + FourDirections.DOWN.direction, backward)
}
