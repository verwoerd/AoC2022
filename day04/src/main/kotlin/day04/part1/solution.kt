package day04.part1

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 04/12/2022
 */
fun day04Part1(input: BufferedReader): Any {
  return input.readInput().count { (left, right) ->
    (left.first in right && left.last in right) || (right.first in left && right.last in left)
  }
}

fun BufferedReader.readInput() = lineSequence()
  .map { line ->
    line.split("-", ",").map { it.toLong() }.let { (lStart, lEnd, rStart, rEnd) -> lStart..lEnd to rStart..rEnd }
  }
