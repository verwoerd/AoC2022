package day15.part1

import Coordinate
import manhattanDistance
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 15/12/2022
 */
fun day15Part1(input: BufferedReader, target: Int = 2000000): Any {
  val spots = mutableSetOf<Coordinate>()
  val ranges = input.lineSequence()
    .flatMap { line ->
      val (lx, ly, rx, ry) = regex.matchEntire(line)!!.destructured
      val sensor = Coordinate(lx.toInt(), ly.toInt())
      val beacon = Coordinate(rx.toInt(), ry.toInt())
      spots.add(sensor)
      spots.add(beacon)
      val distance = manhattanDistance(sensor, beacon)
      (0 until distance).flatMap { dist ->
        val diff = distance - dist
        listOfNotNull(sensor.y + dist to (sensor.x - diff..sensor.x + diff),
          (sensor.y - dist to (sensor.x - diff..sensor.x + diff)).takeIf { dist != 0 })
      }
    }.filter { (sensor) -> sensor == target }
    .fold(mutableSetOf<Int>()) { set, (_, range) ->
      range.forEach { set.add(it) }
      set
    }.size - spots.count { it.y == target }
  return ranges
}

val regex = Regex("Sensor at x=(\\d+), y=(\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)")
