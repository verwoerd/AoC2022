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
  val floor = mutableMapOf<Int, MutableList<Char>>()
  repeat(stacks) {
    floor[it+1]= mutableListOf()
  }
  lineSequence().takeWhile { it.contains("[") }.forEach { line ->
    repeat(stacks) {
      if (line.length > 4 * it + 1 && line[4 * it + 1] != ' ') {
        floor[it + 1]!!.add(line[4 * it + 1])
      }
    }
  }
  floor.forEach { (_, it) -> it.reverse() }
  lineSequence().drop(2)
    .map { current ->
      REGEX.matchEntire(current)!!.groupValues.drop(1).map { it.toInt() }
    }.forEach { (num, from, to) -> craneAlgorithm(floor, num, from, to) }
  return floor.mapNotNull { it.value.lastOrNull() }.joinToString("")
}

val REGEX = Regex("move (\\d+) from (\\d+) to (\\d+)")
