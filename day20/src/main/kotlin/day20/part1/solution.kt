package day20.part1

import java.io.BufferedReader
import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO
import java.util.LinkedList

/**
 * @author verwoerd
 * @since 20/12/2022
 */
fun day20Part1(input: BufferedReader): Any {
  return input.calculateResult()
}

fun BufferedReader.calculateResult(times: Int = 1, key: BigInteger = ONE): BigInteger {
  val data = lineSequence()
    .mapIndexed { index, it -> index to it.toBigInteger() * key }
    .toCollection(LinkedList())
  val order = data.toList()
  val size = (data.size - 1).toBigInteger()
  repeat(times) {
    order.forEach { (original, value) ->
      val index = data.indexOfFirst { original == it.first }
      data.removeAt(index)
      var next = (index.toBigInteger() + value) % size
      if (next <= ZERO)
        next += size
      data.add(next.toInt(), original to value)
    }
  }

  val indexOfNull = data.indexOfFirst { it.second == ZERO }
  val thousand = data[(1000 + indexOfNull) % data.size].second
  val twoThousand = data[(2000 + indexOfNull) % data.size].second
  val threeThousand = data[(3000 + indexOfNull) % data.size].second
  return thousand + twoThousand + threeThousand
}
