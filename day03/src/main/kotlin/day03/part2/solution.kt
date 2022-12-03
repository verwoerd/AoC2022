package day03.part2

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 03/12/2022
 */
fun day03Part2(input: BufferedReader): Any {
  return input.lineSequence()
    .windowed(size = 3, step = 3)
    .map { (left, middle, right) ->
      left.toCharArray().toSet() intersect middle.toCharArray().toSet() intersect right.toCharArray().toSet()
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
