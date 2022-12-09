package day09.part2

import day09.part1.readSimulation
import origin
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 09/12/2022
 */
fun day09Part2(input: BufferedReader): Any = input.readSimulation()
  .fold(Array(10) { origin } to mutableListOf(origin)) { (rope, path), (direction, times) ->
    repeat(times) {
      rope[0] += direction.direction
      (1 until rope.size).forEach {
        val diff = rope[it - 1] - rope[it]
        if (diff.x !in (-1..1) || diff.y !in (-1..1)) {
          rope[it] += diff.normalize()
          if (it == 9) {
            path.add(rope[it])
          }
        }
      }
    }
    rope to path
  }.second.distinct().size

