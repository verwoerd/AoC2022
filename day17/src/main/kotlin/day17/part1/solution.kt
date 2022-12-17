package day17.part1

import Coordinate
import FourDirections
import yRange
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 17/12/2022
 */
fun day17Part1(input: BufferedReader): Any {
  val instructions = input.readLine().toCharArray()
  val area = mutableMapOf<Coordinate, Char>().withDefault { '.' }
  val xRange = (1..7)
  area[Coordinate(1, 0)] = '-'
  area[Coordinate(2, 0)] = '-'
  area[Coordinate(3, 0)] = '-'
  area[Coordinate(4, 0)] = '-'
  area[Coordinate(5, 0)] = '-'
  area[Coordinate(6, 0)] = '-'
  area[Coordinate(7, 0)] = '-'
  val sequence = sequence<RockBlock> {
    while (true) {
      yield(RockBlock.MINUS)
      yield(RockBlock.CROSS)
      yield(RockBlock.LSHAPE)
      yield(RockBlock.OR)
      yield(RockBlock.BLOCK)
    }
  }
  val directionSequence = iterator {
    while (true) {
      instructions.forEach { yield(it) }
    }
  }
  var done: Boolean
  sequence.take(2022).forEach { current ->
    done = false
    val yRange = area.yRange().let { (l, r) -> l..r }

    var bottomLeft = current.getStart(xRange, yRange)
    while (!done) {
      val direction = directionSequence.next()
      bottomLeft = current.makeMove(bottomLeft, xRange, direction, area)
      val next = current.dropDown(bottomLeft, area)
      done = bottomLeft == next
      bottomLeft = next

    }
  }
//  area.printReversedMap { (it ?: ".").toString() }
  return area.yRange().second
}

enum class RockBlock(
  val xDimension: Int,
  val yDimension: Int,
  val skipBlocks: Set<Coordinate>,
  val bottomBlocks: Set<Coordinate>,
  val leftBlocks: Set<Coordinate>,
  val rightBlock: Set<Coordinate>
) {
  MINUS(
    4,
    1,
    emptySet(),
    setOf(Coordinate(0, 0), Coordinate(1, 0), Coordinate(2, 0), Coordinate(3, 0)),
    setOf(Coordinate(0, 0)),
    setOf(Coordinate(3, 0))
  ),
  CROSS(
    3,
    3,
    setOf(Coordinate(0, 0), Coordinate(0, 2), Coordinate(2, 0), Coordinate(2, 2)),
    setOf(Coordinate(1, 0), Coordinate(0, 1), Coordinate(2, 1)),
    setOf(Coordinate(0, 1), Coordinate(1, 0), Coordinate(1, 2)),
    setOf(Coordinate(2, 1), Coordinate(1, 0), Coordinate(1, 2))
  ),
  LSHAPE(
    3,
    3,
    setOf(Coordinate(0, 1), Coordinate(0, 2), Coordinate(1, 1), Coordinate(1, 2)),
    setOf(Coordinate(0, 0), Coordinate(1, 0), Coordinate(2, 0)),
    setOf(Coordinate(0, 0), Coordinate(2, 1), Coordinate(2, 2)),
    setOf(Coordinate(2, 0), Coordinate(2, 1), Coordinate(2, 2))
  ),
  OR(
    1, 4, emptySet(), setOf(Coordinate(0, 0)),
    setOf(Coordinate(0, 0), Coordinate(0, 1), Coordinate(0, 2), Coordinate(0, 3)),
    setOf(Coordinate(0, 0), Coordinate(0, 1), Coordinate(0, 2), Coordinate(0, 3)),
  ),
  BLOCK(
    2,
    2,
    emptySet(),
    setOf(Coordinate(0, 0), Coordinate(1, 0)),
    setOf(Coordinate(0, 0), Coordinate(0, 1)),
    setOf(Coordinate(1, 0), Coordinate(1, 1))
  );

  fun getStart(xRange: IntRange, yRange: IntRange): Coordinate {
    return Coordinate(xRange.first + 2, yRange.last + 4)
  }

  fun makeMove(
    location: Coordinate,
    xRange: IntRange,
    direction: Char,
    area: MutableMap<Coordinate, Char>
  ): Coordinate {
    return when (direction) {
      '>' -> when {
        rightBlock.map { location + it }.map { it.plusX(1) }
          .all { it.x in xRange && area.getValue(it) == '.' } -> location + FourDirections.RIGHT.direction

        else -> location
      }

      '<' -> when {
        leftBlocks.map { location + it }.map { it.plusX(-1) }
          .all { it.x in xRange && area.getValue(it) == '.' } -> location + FourDirections.LEFT.direction

        else -> location
      }

      else -> error("Invalid direction: $direction")
    }
  }

  fun dropDown(location: Coordinate, area: MutableMap<Coordinate, Char>): Coordinate {
    return when {
      bottomBlocks.map { it + location + FourDirections.DOWN.direction }
        .any { area.getValue(it) != '.' || it.y == 0 } -> location.also { area.drawRock(location, this) }

      else -> location + FourDirections.DOWN.direction
    }


  }

  fun reachedBottom(location: Coordinate, area: MutableMap<Coordinate, Char>): Boolean {
    return !bottomBlocks
      .map { it + location }
      .all { area.getValue(it) == '.' }
  }
}

fun MutableMap<Coordinate, Char>.drawRock(location: Coordinate, block: RockBlock) {
//  println("Drawing $block at $location")
  (0 until block.yDimension).forEach { y ->
    (0 until block.xDimension).forEach { x ->
      val diff = Coordinate(x, y)
      if (diff !in block.skipBlocks) {
        if (getValue(location + diff) == '#') error("Painting on existing occupaied tile, $location, $block")
        put(location + diff, '#')
      }
    }
  }
}
//3137 low
