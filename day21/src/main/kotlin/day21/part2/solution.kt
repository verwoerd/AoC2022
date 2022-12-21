package day21.part2

import day21.part1.Monkey
import day21.part1.solveEquation
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 21/12/2022
 */
fun day21Part2(input: BufferedReader): Any {
  val monkeys = Monkey.fromInput(input)
  val monkeysMap = monkeys.associateBy { it.name }.toMutableMap()
  monkeysMap.remove("humn")
  val leftRoot = monkeysMap[monkeysMap["root"]!!.left]!!
  var leftMonkey = monkeysMap.findDependants(leftRoot)
  val stack = mutableListOf<Monkey>()
  while (leftMonkey != null && leftMonkey != leftRoot.name) {
    println("Found monkey $leftMonkey")
    stack.add(monkeysMap[leftMonkey]!!)
    leftMonkey = monkeysMap.findDependants(leftRoot, leftMonkey)
  }
  stack.add(leftRoot)
  val rightValue = monkeysMap.solveEquation(monkeysMap[monkeysMap["root"]!!.right]!!)
  val problemSet = stack.map { it.name }.toSet()
  return stack.foldRight(-rightValue) { monkey, acc ->
    println("[$monkey] $acc")
    if (monkey.right == "humn") {
      val left = monkeysMap.solveEquation(monkeysMap[monkey.left]!!)
      println("[left] $left")
      when (monkey.operator) {
        "+" -> acc - left
        "-" -> acc + left
        "*" -> acc / left
        else -> acc * left
      }
    } else if (monkey.left == "humn") {
      val right = monkeysMap.solveEquation(monkeysMap[monkey.right]!!)
      println("[right] $right")
      when (monkey.operator) {
        "+" -> acc - right
        "-" -> acc + right
        "*" -> acc / right
        else -> acc * right
      }
    } else if (monkey.left !in problemSet) {
      val left = monkeysMap.solveEquation(monkeysMap[monkey.left]!!)
      println("[left] $left")
      when (monkey.operator) {
        "+" -> acc - left
        "-" -> acc + left
        "*" -> acc / left
        else -> acc * left
      }
    } else {
      val right = monkeysMap.solveEquation(monkeysMap[monkey.right]!!)
      println("[right] $right")
      when (monkey.operator) {
        "+" -> acc - right
        "-" -> acc + right
        "*" -> acc / right
        else -> acc * right
      }
    }
  }
}

fun MutableMap<String, Monkey>.findDependants(monkey: Monkey, target: String = "humn"): String? {
  if (monkey.hasValue()) return null
  if (monkey.left == target || monkey.right == target) {
    return monkey.name
  }
  return findDependants(get(monkey.left)!!, target) ?: findDependants(get(monkey.right)!!, target)
}


//195991948598816
// 8204572764846
// 8204572764413
