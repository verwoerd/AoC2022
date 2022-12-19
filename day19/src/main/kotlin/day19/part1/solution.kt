package day19.part1

import priorityQueueOf
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 19/12/2022
 */
fun day19Part1(input: BufferedReader): Any {
  val blueprints = Blueprint.fromInput(input)
  return blueprints.sumOf { blueprint ->
    val initial = State(blueprint)
    val seen = mutableSetOf(initial)
    val queue = priorityQueueOf(initial)
    val max = mutableMapOf(0 to 0).withDefault { 0 }
    while (queue.isNotEmpty()) {
      val current = queue.remove()
      if (max.getValue(current.time) > current.geode) continue
      max[current.time] = max.getValue(current.time).coerceAtLeast(current.geode)
      if (current.time >= 24) continue
      current.getNextStates().filter { seen.add(it) }.toCollection(queue)
    }
    blueprint.id * max[24]!!
  }
}


data class State(
  val blueprint: Blueprint,
  val time: Int = 0,
  val ore: Int = 0,
  val clay: Int = 0,
  val obsidian: Int = 0,
  val geode: Int = 0,
  val miningBot: Int = 1,
  val clayBot: Int = 0,
  val obsidianBot: Int = 0,
  val geodeBot: Int = 0,
) : Comparable<State> {
  fun getNextStates(): List<State> {
    val newOre = miningBot
    val newClay = clayBot
    val newObsidian = obsidianBot
    val newGeode = geodeBot
    if (ore >= blueprint.geodeOreCost && obsidian >= blueprint.geodeObsidianCost) {
      return listOf(
        copy(
          time = time + 1,
          ore = ore + newOre - blueprint.geodeOreCost,
          clay = clay + newClay,
          obsidian = obsidian + newObsidian - blueprint.geodeObsidianCost,
          geode = geode + newGeode,
          geodeBot = geodeBot + 1
        )
      )
    }
    return listOfNotNull(
      copy(
        time = time + 1,
        ore = ore + newOre - blueprint.obsidianOreCost,
        clay = clay + newClay - blueprint.obsidianClayCost,
        obsidian = obsidian + newObsidian,
        geode = geode + newGeode,
        obsidianBot = obsidianBot + 1
      ).takeIf { ore >= blueprint.obsidianOreCost && clay >= blueprint.obsidianClayCost && obsidianBot < blueprint.maxObsidian },
      copy(
        time = time + 1,
        ore = ore + newOre - blueprint.clayCost,
        clay = clay + newClay,
        obsidian = obsidian + newObsidian,
        geode = geode + newGeode,
        clayBot = clayBot + 1
      ).takeIf { ore >= blueprint.clayCost && clayBot < blueprint.maxClay },
      copy(
        time = time + 1,
        ore = ore + newOre - blueprint.oreCost,
        clay = clay + newClay,
        obsidian = obsidian + newObsidian,
        geode = geode + newGeode,
        miningBot = miningBot + 1
      ).takeIf { ore >= blueprint.oreCost && miningBot < blueprint.maxOre },
      copy(
        time = time + 1,
        ore = ore + newOre,
        clay = clay + newClay,
        obsidian = obsidian + newObsidian,
        geode = geode + newGeode,
      ).takeIf {
        ore < blueprint.oreCost
            || (clay < blueprint.obsidianClayCost && ore < blueprint.obsidianOreCost)
            || (obsidian < blueprint.geodeObsidianCost && ore < blueprint.geodeOreCost)
      }
    )
  }

  override fun compareTo(other: State): Int {
    return -1 * when (val g = this.geodeBot.compareTo(other.geodeBot)) {
      0 -> when (val gc = this.geode.compareTo(other.geode)) {
        0 -> when (val o = this.obsidianBot.compareTo(other.obsidianBot)) {
          0 -> when (val oc = this.obsidian.compareTo(other.obsidian)) {
            0 -> when (val c = this.clayBot.compareTo(other.clayBot)) {
              0 -> when (val cc = this.clay.compareTo(other.clay)) {
                0 -> when (val m = this.miningBot.compareTo(other.miningBot)) {
                  0 -> this.ore.compareTo(other.ore)
                  else -> m
                }

                else -> cc
              }

              else -> c
            }

            else -> oc
          }

          else -> o
        }

        else -> gc
      }

      else -> g
    }
  }

  override fun toString(): String {
    return "State(time=$time, ore=$ore, clay=$clay, obsidian=$obsidian, geode=$geode, miningBot=$miningBot, clayBot=$clayBot, obsidianBot=$obsidianBot, geodeBot=$geodeBot)"
  }

}

data class Blueprint(
  val id: Int,
  val oreCost: Int,
  val clayCost: Int,
  val obsidianOreCost: Int,
  val obsidianClayCost: Int,
  val geodeOreCost: Int,
  val geodeObsidianCost: Int
) {
  val maxOre by lazy { oreCost.coerceAtLeast(clayCost).coerceAtLeast(obsidianOreCost).coerceAtLeast(geodeOreCost) }
  val maxClay by lazy { obsidianClayCost }
  val maxObsidian by lazy { geodeObsidianCost }

  companion object {
    private val regex =
      Regex("Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.")

    fun fromInput(input: BufferedReader) =
      input.lineSequence().map { fromLine(it) }.toList()

    private fun fromLine(line: String) =
      regex.matchEntire(line)!!.destructured.let { (id, ore, clay, oo, oc, go, cg) ->
        Blueprint(
          id = id.toInt(),
          oreCost = ore.toInt(),
          clayCost = clay.toInt(),
          obsidianOreCost = oo.toInt(),
          obsidianClayCost = oc.toInt(),
          geodeOreCost = go.toInt(),
          geodeObsidianCost = cg.toInt()
        )
      }
  }
}
