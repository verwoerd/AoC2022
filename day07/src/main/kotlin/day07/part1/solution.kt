package day07.part1

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 07/12/2022
 */
val REGEX = Regex("(\\d+) (.+)")

fun day07Part1(input: BufferedReader): Any {
  return input.parseFsListing().values.filter { it < 100000L }.sum()
}

fun BufferedReader.parseFsListing(): Map<String, Long> =
  lineSequence().fold(mutableListOf<String>() to mutableMapOf<String, Long>()) { (dir, fs), s ->
    when {
      s.startsWith("$ cd ..") -> dir.also { it.removeLast() } to fs
      s.startsWith("$ cd /") ->  mutableListOf("/") to fs
      s.startsWith("$ cd") -> dir.also { it.add(s.substring(5)) } to fs
      s.startsWith("$ ls") -> dir to fs
      s.startsWith("dir") -> dir to fs
      else -> dir to fs.also {
        val size = REGEX.matchEntire(s)!!.groupValues[1].toLong()
        (1..dir.size).forEach {
          val directory = dir.take(it).joinToString("/")
          fs[directory] = (fs[directory] ?: 0L) + size
        }
      }
    }
  }.second
