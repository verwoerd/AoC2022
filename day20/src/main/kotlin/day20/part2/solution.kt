package day20.part2

import day20.part1.calculateResult
import java.io.BufferedReader
import java.math.BigInteger.valueOf

/**
 * @author verwoerd
 * @since 20/12/2022
 */
fun day20Part2(input: BufferedReader): Any {
  val key = valueOf(811589153L)
  return input.calculateResult(10, key)
}
