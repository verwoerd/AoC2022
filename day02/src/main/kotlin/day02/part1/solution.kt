package day02.part1

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 02/12/2022
 */
fun day02Part1(input: BufferedReader): Any {
  return input.lineSequence().fold(0) { acc, line ->
    val (left, right) = line.split(" ", limit = 2)
    acc + gameResult(left, right)
  }
}

fun gameResult(left: String, right: String) =
  when (left) {
    "A" -> when (right) { // rock
      "X" -> 3 + 1 //rock
      "Y" -> 6 + 2 // paper
      "Z" -> 0 + 3 // scissors
      else -> error("invalid input: $right")
    }

    "B" -> when (right) { // paper
      "X" -> 0 + 1 //rock
      "Y" -> 3 + 2 // paper
      "Z" -> 6 + 3 // scissors
      else -> error("invalid input: $right")
    }

    "C" -> when (right) { // scissors
      "X" -> 6 + 1 //rock
      "Y" -> 0 + 2 // paper
      "Z" -> 3 + 3 // scissors
      else -> error("invalid input: $right")
    }

    else -> error("Invalid input: $left")
  }
