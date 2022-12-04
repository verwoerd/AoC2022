package day04.part2

import day04.part1.readInput
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 04/12/2022
 */
fun day04Part2(input: BufferedReader): Any {
  return input.readInput().count { (left, right) ->
    left.first in right || left.last in right || right.first in left || right.last in left
  }
}
