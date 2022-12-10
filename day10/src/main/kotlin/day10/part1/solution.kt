package day10.part1

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 10/12/2022
 */
fun day10Part1(input: BufferedReader): Any {
  val result = input.generateCycleValues()
  return listOf(20, 60, 100, 140, 180, 220).sumOf {
    it * (result[it] ?: result[it - 1]!!)
  }
}

fun BufferedReader.generateCycleValues() = lineSequence().runningFold(1 to 1) { (cycle, x), line ->
  when {
    line.startsWith("noop") -> cycle + 1 to x
    line.startsWith("addx") -> cycle + 2 to x + line.drop(5).toInt()
    else -> error("invalid input")
  }
}.toMap()
