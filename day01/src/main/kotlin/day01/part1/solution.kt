package day01.part1

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 01/12/2022
 */
fun day01Part1(input: BufferedReader): Any {
  val values = input.readInput()
  return values.max()
}

fun BufferedReader.readInput() = lineSequence()
  .fold(mutableListOf<Long>() to 0L) { (list, acc), line ->
    when {
      line.isBlank() -> list.also{ it.add(acc) } to 0L
      else -> list to acc + line.toLong()
    }
  }.let { (list, acc) -> list.also { it.add(acc) } }
