package day10.part2

import day10.part1.generateCycleValues
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 10/12/2022
 */
fun day10Part2(input: BufferedReader): Any {
  val result = input.generateCycleValues()
  return (0..5).joinToString("\n") { y ->
    (1..40).joinToString("") { x ->
      val it = 40 * y + x
      val sprite = result[it] ?: result[it - 1]!!
      when (x-1) {
        in (sprite - 1..sprite + 1) -> "#"
        else -> "."
      }
    }
  }
}
