package day05.part1

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 05/12/2022
 */
fun day05Part1(input: BufferedReader, stacks: Int = 9): Any {
  return input.calculateTopCrates(stacks) { floor, num, from, to ->
      floor[to]!!.addAll(floor[from]!!.takeLast(num).reversed())
      floor[from] = floor[from]!!.dropLast(num).toMutableList()
  }
}

fun BufferedReader.calculateTopCrates(
  stacks: Int,
  craneAlgorithm: (MutableMap<Int, MutableList<Char>>, num: Int, from: Int, to: Int) -> Unit
): String {
  var line = this.readLine()!!
  val floor = mutableMapOf(
    1 to mutableListOf<Char>(),
    2 to mutableListOf(),
    3 to mutableListOf(),
    4 to mutableListOf(),
    5 to mutableListOf(),
    6 to mutableListOf(),
    7 to mutableListOf(),
    8 to mutableListOf(),
    9 to mutableListOf()
  )
  while (line.contains("[")) {
    repeat(stacks) {
      if (line.length > 4 * it + 1 && line[4 * it + 1] != ' ') {
        floor[it + 1]!!.add(line[4 * it + 1])
      }
    }
    line = this.readLine()!!
  }
  this.readLine() // skip empty line
  floor.forEach { (_, it) -> it.reverse() }
  lineSequence()
    .map { current ->
      REGEX.matchEntire(current)!!.groupValues.drop(1).map { it.toInt() }
    }.forEach { (num, from, to) -> craneAlgorithm(floor, num, from, to) }
  return floor.mapNotNull { it.value.lastOrNull() }.joinToString("")
}

val REGEX = Regex("move (\\d+) from (\\d+) to (\\d+)")
