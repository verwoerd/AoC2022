package day03.part1

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 03/12/2022
 */
fun day03Part1(input: BufferedReader): Any {
  return input.lineSequence().map {
    it.take(it.length / 2).toCharArray() intersect it.drop(it.length / 2).toCharArray().toSet()
  }.sumOf { doubles ->
    doubles.sumOf {
      when (it) {
        in 'a'..'z' -> it - 'a' + 1
        in 'A'..'Z' -> it - 'A' + 27
        else -> 0
      }
    }
  }
}
