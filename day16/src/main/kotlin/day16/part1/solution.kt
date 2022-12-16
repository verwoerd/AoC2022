package day16.part1

import priorityQueueOf
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 16/12/2022
 */
fun day16Part1(input: BufferedReader): Any {
  val rates = mutableMapOf<String, Long>()
  val connections = mutableMapOf<String, List<String>>()
  input.lineSequence()
    .forEach { line ->
      val (label, weight, paths) = regex.matchEntire(line)!!.destructured
      rates[label] = weight.toLong()
      connections[label] = paths.split(", ")
    }
  val labels = rates.keys.withIndex().associate { (index, s) -> index to s }
  val labelsReversed = labels.entries.associate { (k, v) -> v to k }


  val start = "AA"
  val distances = Array(rates.keys.size) { IntArray(rates.keys.size) { Int.MAX_VALUE / 2 - 1 } }
  labels.entries.forEach { (index, label) ->
    connections[label]!!.map {
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

  val targets = rates.entries.filter { (_, v) -> v != 0L }.map { (k) -> k }.toMutableSet()


  val queue =
    priorityQueueOf(Comparator { (a), (b) -> a.compareTo(b) }, Triple(0, start, Triple(31, 0L, targets.toMutableSet())))
  var maxRelease = 0L
  var maxTimeLeft = 0
  while (queue.isNotEmpty()) {
    val (moveTime, current, pair) = queue.remove()
    var (time, release, leftToVisit) = pair
    val rate = rates[current]!!
    time -= (moveTime + 1)
    if (time <= 0) continue
    release += time * rate
    leftToVisit.remove(current)
    if (release < maxRelease && time < maxTimeLeft) continue
    if (release > maxRelease) {
      maxRelease = release
      maxTimeLeft = time
    }
    val currentIndex = labelsReversed[current]!!
    val nextSteps = leftToVisit.map { labelsReversed[it]!! to it }.map { (index, label) ->
      Triple(
        distances[currentIndex][index],
        label,
        Triple(time, release, leftToVisit.toMutableSet())
      )
    }
    queue.addAll(nextSteps)

  }
  return maxRelease
}

val regex = Regex("Valve (\\w+) has flow rate=(\\d+); tunnels? leads? to valves? (.*)")

