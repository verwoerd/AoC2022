package day02.part2

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 02/12/2022
 */
fun day02Part2(input: BufferedReader): Any {
  return input.lineSequence().fold(0) { acc, line ->
    val (left, right) = line.split(" ", limit = 2)
    acc + gameResult(left, right)
  }
}

// Copy-Paste is your friend
fun gameResult(left: String, right: String) =
  when (left) {
    "A" -> when (right) { // rock
      "Y" -> 3 + 1  //rock
      "Z" -> 6 + 2 // paper
      "X" -> 0 + 3 // scissors
      else -> error("invalid input: $right")
    }

    "B" -> when (right) { // paper
      "X" -> 0 + 1  //rock
      "Y" -> 3 + 2 // paper
      "Z" -> 6 + 3// scissors
      else -> error("invalid input: $right")
    }


    "C" -> when (right) { // siccors
      "Z" -> 6 + 1  //rock
      "X" -> 0 + 2 // paper
      "Y" -> 3 + 3// scissors
      else -> error("invalid input: $right")
    }

    else -> error("Invalid input: $left")
  }
