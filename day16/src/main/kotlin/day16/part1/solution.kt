package day16.part1

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 16/12/2022
 */
fun day16Part1(input: BufferedReader): Any {
  val (rates, connections) = input.readInput()
  val start = "AA"
  val (distances, labelsReversed) = connections.floydWarshall()
  val targets = rates.entries.filter { (_, v) -> v != 0L }.map { (k) -> k }.toMutableSet()
  var maxRelease = 0L
  fun dfs(release: Long, location: String, seen: Set<String>, time: Int) {
    maxRelease = maxRelease.coerceAtLeast(release)
    targets.filter { it !in seen }.forEach {
      val distance = distances[labelsReversed[location]!!][labelsReversed[it]!!]
      if (time - distance - 1 > 0) {
        dfs(release + (time - distance - 1) * rates[it]!!, it, seen + it, time - distance - 1)
      }
    }
  }
  dfs(0L, start, emptySet(), 30)
  return maxRelease
}

val regex = Regex("Valve (\\w+) has flow rate=(\\d+); tunnels? leads? to valves? (.*)")

fun BufferedReader.readInput(): Pair<MutableMap<String, Long>, MutableMap<String, List<String>>> {
  val rates = mutableMapOf<String, Long>()
  val connections = mutableMapOf<String, List<String>>()
  lineSequence()
    .forEach { line ->
      val (label, weight, paths) = regex.matchEntire(line)!!.destructured
      rates[label] = weight.toLong()
      connections[label] = paths.split(", ")
    }
  return rates to connections
}

fun Map<String, List<String>>.floydWarshall(): Pair<Array<IntArray>, Map<String, Int>> {
  val labels = keys.withIndex().associate { (index, s) -> index to s }
  val labelsReversed = labels.entries.associate { (k, v) -> v to k }
  val distances = Array(keys.size) { IntArray(keys.size) { Int.MAX_VALUE / 2 - 1 } }
  labels.entries.forEach { (index, label) ->
    get(label)!!.map {
      distances[index][labelsReversed[it]!!] = 1
    }
  }

  distances.indices.forEach { i -> distances[i][i] = 0 }
  distances.indices.forEach { k ->
    distances.indices.forEach { i ->
      distances.indices.forEach { j ->
        if (distances[i][j] > distances[i][k] + distances[k][j]) {
          distances[i][j] = distances[i][k] + distances[k][j]
        }
      }
    }
  }
  return distances to labelsReversed
}
