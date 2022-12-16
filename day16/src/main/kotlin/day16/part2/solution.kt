package day16.part2

import day16.part1.floydWarshall
import day16.part1.readInput
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 16/12/2022
 */
fun day16Part2(input: BufferedReader): Any {
  val (rates, connections) = input.readInput()
  val start = "AA"
  val (distances, labelsReversed) = connections.floydWarshall()
  val targets = rates.entries.filter { (_, v) -> v != 0L }.map { (k) -> k }.toMutableSet()
  var maxRelease = 0L
  fun dfs(release: Long, location: String, seen: Set<String>, time: Int, elephant: Boolean = false) {
    maxRelease = maxRelease.coerceAtLeast(release)
    targets.filter { it !in seen }.forEach {
      val distance = distances[labelsReversed[location]!!][labelsReversed[it]!!]
      if (time - distance - 1 > 0) {
        dfs(release + (time - distance - 1) * rates[it]!!, it, seen + it, time - distance - 1, elephant)
      }
    }
    if (elephant) dfs(release, "AA", seen, 26)
  }
  dfs(0L, start, emptySet(), 26, true)
  return maxRelease
}

