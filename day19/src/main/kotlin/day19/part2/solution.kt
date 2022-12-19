package day19.part2

import day19.part1.Blueprint
import day19.part1.State
import priorityQueueOf
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 19/12/2022
 */
fun day19Part2(input: BufferedReader): Any {
  val blueprints = Blueprint.fromInput(input).take(3)
  return blueprints.map { blueprint ->
    val initial = State(blueprint)
    val seen = mutableSetOf(initial)
    val queue = priorityQueueOf(initial)
    val max = mutableMapOf(0 to 0).withDefault { 0 }
    while (queue.isNotEmpty()) {
      val current = queue.remove()
      if (max.getValue(current.time) > current.geode) continue
      if (current.time >= 32) {
        max[current.time] = max.getValue(current.time).coerceAtLeast(current.geode)
        continue
      }
      max[current.time] = max.getValue(current.time).coerceAtLeast(current.geode)
      current.getNextStates().filter { it.time < 33 }.filter { seen.add(it) }.toCollection(queue)
    }

    max[32]!!
  }.fold(1L) { acc, i -> acc * i }
}
