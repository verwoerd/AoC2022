package day07.part2

import day07.part1.parseFsListing
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 07/12/2022
 */
fun day07Part2(input: BufferedReader): Any {
  val fs = input.parseFsListing()
  val sizeAvailable = 70000000 - fs["/"]!!
  return fs.values.filter { sizeAvailable + it > 30000000 }.min()
}

