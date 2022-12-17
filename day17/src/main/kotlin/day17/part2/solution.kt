package day17.part2

import Coordinate
import day17.part1.RockBlock
import toInt
import xRange
import yRange
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 17/12/2022
 */
fun day17Part2(input: BufferedReader): Any {
  val instructions = input.readLine().toCharArray()
  val iterations = 1000000000000L
  fun CharArray.simulateMoves(loopSearch: Int = 1, take: Int = Int.MAX_VALUE): Pair<Long, Long> {
    var area = mutableMapOf<Coordinate, Char>().withDefault { '.' }
    val xRange = (1..7)
    area[Coordinate(1, 0)] = '#'
    area[Coordinate(2, 0)] = '#'
    area[Coordinate(3, 0)] = '#'
    area[Coordinate(4, 0)] = '#'
    area[Coordinate(5, 0)] = '#'
    area[Coordinate(6, 0)] = '#'
    area[Coordinate(7, 0)] = '#'
    val sequence = sequence {
      while (true) {
        yield(RockBlock.MINUS)
        yield(RockBlock.CROSS)
        yield(RockBlock.LSHAPE)
        yield(RockBlock.OR)
        yield(RockBlock.BLOCK)
      }
    }

    var done: Boolean
    var loopFound = 0
    var rocksFallen = 0L
    var heightReached = 0L
    var index = 0
    val seen = mutableSetOf<Triple<Map<Coordinate, Char>, RockBlock, Int>>()

    sequence.take(take).takeWhile { loopFound < loopSearch }.forEach { current ->
      done = false
      val yRange = area.yRange().let { (l, r) -> l..r }
      var bottomLeft = current.getStart(xRange, yRange)
      while (!done) {
        val direction = get(index)
        bottomLeft = current.makeMove(bottomLeft, xRange, direction, area)
        val next = current.dropDown(bottomLeft, area)
        done = bottomLeft == next
        bottomLeft = next
        val (a, h) = area.prune()
        heightReached += h
        area = a.toMutableMap().withDefault { '.' }
        bottomLeft = bottomLeft.plusY(-h)
        if (!seen.add(Triple(area.toMap(), current, index))) {
          loopFound++
          seen.clear()
          if (loopFound == loopSearch) {
            return rocksFallen + done.toInt() to heightReached + area.yRange().second
          }
        }
        index = (index + 1) % size
      }
      rocksFallen++
    }
    return rocksFallen to heightReached + area.yRange().second
  }
  val (rocksFirstLoop, heightFirstLoop) = instructions.simulateMoves(1)
  val (rocksSecondLoop, heightSecondLoop) = instructions.simulateMoves(2)
  var rocks = rocksFirstLoop
  var height = heightFirstLoop
  val times = (iterations - rocksFirstLoop) / (rocksSecondLoop - rocksFirstLoop) + 1
  rocks += times * (rocksSecondLoop - rocksFirstLoop)
  height += times * (heightSecondLoop - heightFirstLoop)

  val (_, heightFix) = instructions.simulateMoves(1, take = (rocksFirstLoop - (rocks - iterations)).toInt())
  return height - (heightFirstLoop - heightFix)
}

fun Map<Coordinate, Char>.prune(): Pair<MutableMap<Coordinate, Char>, Int> {
  val yPrune = xRange().let { (l, r) -> l..r }.minOf { x ->
    keys.filter { it.x == x }.maxOfOrNull { it.y } ?: 0
  }
  return entries.filter { it.key.y - yPrune >= 0 }.associate { (k, v) -> k.plusY(-yPrune) to v }.toMutableMap()
    .withDefault { '.' } to yPrune
}
