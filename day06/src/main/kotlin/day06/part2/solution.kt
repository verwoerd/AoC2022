package day06.part2

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 06/12/2022
 */
fun day06Part2(input: BufferedReader): Any {
  return input.readLine().windowed(size = 14).indexOfFirst { it.chars().distinct().count() == 14L } + 14
}
