package days

import arrow.core.memoize
import utils.*

fun breakPatterns(patterns:List<String>, towels:List<String>): Long {
  val partialPatterns = mutableListOf<String>()
  if (patterns.all { it.isEmpty() }) return patterns.size.toLong()

  patterns.forEach { pattern ->
    towels.forEach { towel -> if (pattern.startsWith(towel)) partialPatterns.add(pattern.removePrefix(towel)) }
  }

  return if (partialPatterns.isEmpty()) 0 else partialPatterns.sumOf { memoizedbreakPatterns(listOf(it),towels) }
}
val memoizedbreakPatterns = ::breakPatterns.memoize()

suspend fun main() = AdventOfCode(day = 19, year = 2024) {
  val (towels, patterns) = input.split("\n\n").toPair().let { it.first.split(", ") to it.second.lines() }
  part1 = patterns.count { memoizedbreakPatterns(listOf(it),towels) > 1  }
  part2 = patterns.sumOf { memoizedbreakPatterns(listOf(it),towels)  }
}.start()

