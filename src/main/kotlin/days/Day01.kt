package days

import utils.AdventOfCode
import utils.toPair
import kotlin.math.abs

suspend fun main () = AdventOfCode(day = 1, year = 2024) {
   val (first, second) = input.lines().map {
       it.split("   ").map { nr -> nr.toInt()}.toPair()
   }.unzip().let {
       it.first.sorted() to it.second.sorted()
   }
   part1 = first.zip(second).map { abs(it.first - it.second) }.sum()

   val occurrences = first.map {
       second.count { secondItem -> it == secondItem }
   }
   part2 = first.indices.sumOf { occurrences[it] * first[it] }
}.start()