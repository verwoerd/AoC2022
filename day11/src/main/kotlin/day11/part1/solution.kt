package day11.part1

import java.io.BufferedReader
import java.util.LinkedList

/**
 * @author verwoerd
 * @since 11/12/2022
 */
fun day11Part1(input: BufferedReader): Any = input.doMonkeySimulation(20)

fun BufferedReader.doMonkeySimulation(times: Int, part2: Boolean = false): Long {
  val monkeys = Monkey.fromInput(readText(), part2)
  repeat(times) {
    monkeys.keys.forEach {
      val monkey = monkeys[it]!!
      while (monkey.hasNext()) {
        val (to, worry) = monkey.doRound()
        monkeys[to]!!.items.addLast(worry)
      }
    }
  }
  return monkeys.values.sortedByDescending { it.considered }.take(2).let { (a, b) -> a.considered * b.considered }
}

data class Monkey(
  val id: Int,
  val increase: Long?, // null = squared
  val increaseOperation: Boolean,  // true = plus, false = times
  val divisable: Long,
  val trueTarget: Int,
  val falseTarget: Int,
  val items: LinkedList<Long>,
  var hardMode: Boolean,
  var considered: Long = 0
) {
  companion object {
    val regex = Regex(
      """Monkey (\d+):
 {2}Starting items: (.*)
 {2}Operation: new = old ([*|+]) (\d+|old)
 {2}Test: divisible by (\d+)
 {4}If true: throw to monkey (\d+)
 {4}If false: throw to monkey (\d+)"""
    )

    var divisorSum: Long = 3L
    fun fromInput(input: String, part2: Boolean) =
      regex.findAll(input).map { toMonkey(it, part2) }.associateBy { it.id }
        .also { divisorSum = it.values.fold(1) { a, b -> a * b.divisable } }

    fun toMonkey(match: MatchResult, part2: Boolean): Monkey {
      val (id, items, op, opValue, div, trueValue, falseValue) = match.destructured
      return Monkey(
        id.toInt(),
        opValue.toLongOrNull(),
        op == "+",
        div.toLong(),
        trueValue.toInt(),
        falseValue.toInt(),
        items.split(", ").map { it.toLong() }.toCollection(LinkedList()),
        part2
      )
    }
  }

  fun hasNext() = items.isNotEmpty()

  fun doRound(): Pair<Int, Long> {
    considered++
    var current = items.removeFirst()
    if (increaseOperation) current += (increase ?: current) else current *= (increase ?: current)
    if (hardMode) current %= divisorSum else current /= 3
    return when (current % divisable) {
      0L -> trueTarget
      else -> falseTarget
    } to current
  }
}
