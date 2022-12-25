package day25.part1

import java.io.BufferedReader
import kotlin.math.pow

/**
 * @author verwoerd
 * @since 25/12/22
 */
fun day25Part1(input: BufferedReader): Any {
  val sum = input.lineSequence()
    .map { line ->
      val length = line.length - 1
      line.foldRightIndexed(0L) { index, c, acc ->
        acc + 5.0.pow(length - index).toLong() * when (c) {
          '=' -> -2
          '-' -> -1
          '0' -> 0
          '1' -> 1
          '2' -> 2
          else -> error("unknown digit $c")
        }
      }
    }.sum()
  return rewrite(sum)

}

fun rewrite(num: Long): String = when (num) {
  0L -> ""
  else -> when (num%5L) {
    0L -> rewrite(num / 5) + "0"
    1L -> rewrite(num / 5) + "1"
    2L -> rewrite(num / 5) + "2"
    3L -> rewrite((num + 2) / 5) + "="
    4L -> rewrite((num + 1) / 5) + "-"
    else -> error("no valid representation")
  }
}
