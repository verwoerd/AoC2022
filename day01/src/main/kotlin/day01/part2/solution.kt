package day01.part2

import day01.part1.readInput
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 01/12/2022
 */
fun day01Part2(input: BufferedReader): Any {
  val values = input.readInput()
  return values.sorted().takeLast(3).sum()
}
