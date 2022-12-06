package day06.part1

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 06/12/2022
 */
fun day06Part1(input: BufferedReader): Any {
  return input.readLine().windowed(size = 4).indexOfFirst { it.chars().distinct().count() == 4L } + 4
}
