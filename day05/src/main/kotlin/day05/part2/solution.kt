package day05.part2

import day05.part1.calculateTopCrates
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 05/12/2022
 */
fun day05Part2(input: BufferedReader, stacks: Int = 9): Any {
  return input.calculateTopCrates(stacks) { floor, num, from, to ->
    floor[to]!!.addAll(floor[from]!!.takeLast(num))
    floor[from] = floor[from]!!.dropLast(num).toMutableList()
  }
}
