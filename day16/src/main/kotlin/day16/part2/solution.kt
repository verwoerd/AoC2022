package day16.part2

import day16.part1.regex
import priorityQueueOf
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 16/12/2022
 */
fun day16Part2(input: BufferedReader): Any {
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
  val startStep = ValveStep("AA", "AA", 0, 0, targets.toSet(), emptySet(), 27, 0L)

  val queue =
    priorityQueueOf(Comparator { a, b ->
      -1 * (a.release.compareTo(b.release))
    }, startStep)
  var maxRelease = 0L
  var maxTimeLeft = 0
  while (queue.isNotEmpty()) {
    val current = queue.remove()
    if (current.myMoveTime == current.elephantMoveTime) {
      val time = current.clock - current.myMoveTime - 1
      if (time < 0) continue
      val release = current.release + time * rates[current.myLocation]!! + time * rates[current.elephantLocation]!!
      if (release < maxRelease && time < maxTimeLeft) continue
      if (release > maxRelease) {
        maxRelease = release
        maxTimeLeft = time
        println("[$time][$release] $current")
      }
      val myIndex = labelsReversed[current.myLocation]!!
      val elephantIndex = labelsReversed[current.elephantLocation]!!
      val targetsLeft = current.targetsLeft - setOf(current.myLocation, current.elephantLocation)
      val next = current.targetsLeft
        .map { labelsReversed[it]!! to it }
        .map { (index, label) ->
          current.copy(
            myLocation = label,
            myMoveTime = distances[myIndex][index],
            clock = time,
            release = release,
            targetsLeft = targetsLeft - setOf(label)
          )
        }.flatMap { step ->
          step.targetsLeft
            .map { labelsReversed[it]!! to it }
            .map { (index, label) ->
              step.copy(
                elephantLocation = label, elephantMoveTime = distances[elephantIndex][index],
                targetsLeft = step.targetsLeft - setOf(label)
              )
            }
        }.toCollection(queue)
    } else if (current.myMoveTime < current.elephantMoveTime) {
      val time = current.clock - current.myMoveTime - 1
      if (time < 0) continue
      val release = current.release + time * rates[current.myLocation]!!
      if (release < maxRelease && time < maxTimeLeft) continue
      if (release > maxRelease) {
        maxRelease = release
        maxTimeLeft = time
        println("[$time][$release] $current")
      }
      val myIndex = labelsReversed[current.myLocation]!!
      val targetsLeft = current.targetsLeft - setOf(current.myLocation)
      val next = current.targetsLeft
        .map { labelsReversed[it]!! to it }
        .map { (index, label) ->
          current.copy(
            myLocation = label,
            myMoveTime = distances[myIndex][index],
            elephantMoveTime = current.elephantMoveTime - current.myMoveTime - 1,
            clock = time,
            release = release,
            targetsLeft = targetsLeft - setOf(label)
          )
        }
      if (next.isEmpty()) {
        queue.add(current.copy(myMoveTime = 100, elephantMoveTime = current.elephantMoveTime - current.myMoveTime - 1))
      } else {
        queue.addAll(next)
      }
    } else {
      val time = current.clock - current.elephantMoveTime - 1
      if (time < 0) continue
      val release = current.release + time * rates[current.elephantLocation]!!
      if (release < maxRelease && time < maxTimeLeft) continue
      if (release > maxRelease) {
        maxRelease = release
        maxTimeLeft = time
        println("[$time][$release] $current")
      }
      val elephantIndex = labelsReversed[current.elephantLocation]!!
      val targetsLeft = current.targetsLeft - setOf(current.elephantLocation)
      val next = current.targetsLeft
        .map { labelsReversed[it]!! to it }
        .map { (index, label) ->
          current.copy(
            elephantLocation = label,
            elephantMoveTime = distances[elephantIndex][index],
            myMoveTime = current.myMoveTime - current.elephantMoveTime - 1,
            clock = time,
            release = release,
            targetsLeft = targetsLeft - setOf(label)
          )
        }
      if (next.isEmpty()) {
        queue.add(current.copy(elephantMoveTime = 100, myMoveTime = current.myMoveTime - current.elephantMoveTime - 1))
      } else {
        queue.addAll(next)
      }
    }
  }
  return maxRelease
}

data class ValveStep(
  val myLocation: String,
  val elephantLocation: String,
  val myMoveTime: Int,
  val elephantMoveTime: Int,
  val targetsLeft: Set<String>,
  val targetsVisited: Set<String>,
  val clock: Int,
  val release: Long
)
