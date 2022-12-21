package day21.part1

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 21/12/2022
 */
fun day21Part1(input: BufferedReader): Any {
  val monkeys = Monkey.fromInput(input)
  val monkeysMap = monkeys.associateBy { it.name }.toMutableMap()
  return monkeysMap.solveEquation(monkeysMap["root"]!!)
}

fun MutableMap<String, Monkey>.solveEquation(monkey: Monkey): Long {
  if (monkey.hasValue()) return monkey.value
  val leftValue = solveEquation(get(monkey.left)!!)
  val rightValue = solveEquation(get(monkey.right)!!)
  val value = when (monkey.operator) {
    "+" -> leftValue + rightValue
    "-" -> leftValue - rightValue
    "*" -> leftValue * rightValue
    "/" -> leftValue / rightValue
    else -> error("Invalid operation $monkey")
  }
  put(monkey.name, monkey.copy(value = value))
  return value
}


data class Monkey(
  val name: String,
  val left: String,
  val right: String,
  val operator: String,
  val value: Long
) {
  companion object {
    val numberRegex = Regex("(\\w+): (-?\\d+)")
    val mathRegex = Regex("(\\w+): (\\w+) ([+-/*]) (\\w+)")

    fun fromInput(input: BufferedReader) = input.lineSequence()
      .map { toMonkey(it) }.toList()

    fun toMonkey(line: String): Monkey {
      return when (val numberResult = numberRegex.matchEntire(line)) {
        null -> {
          val (name, left, operator, right) = mathRegex.matchEntire(line)!!.destructured
          Monkey(name, left, right, operator, Long.MAX_VALUE)
        }

        else -> {
          val (name, value) = numberResult.destructured
          Monkey(name, "", "", "", value.toLong())
        }
      }

    }
  }

  fun hasValue(): Boolean {
    return operator.isEmpty() || value != Long.MAX_VALUE
  }
}
