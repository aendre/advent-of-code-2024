package days

import utils.AdventOfCode
import utils.toPair
import kotlin.math.abs

suspend fun day01() {
val example = """3   4
4   3
2   5
1   3
3   9
3   3""";

    AdventOfCode(day = 1, year = 2024) {
       val (first, second) = input.lines().map {
           it.split("   ").map { nr -> nr.toInt()}.toPair()
       }.unzip().let {
           it.first.sorted() to it.second.sorted()
       }
       val diff = first.zip(second).map { abs(it.first - it.second) }
       println("part I  : ${diff.sum()}")

       val occurrences = first.map {
           second.count { secondItem -> it == secondItem }
       }
       val part2 = first.indices.sumOf { occurrences[it] * first[it] }
       println("part II : $part2")
    }.start()
}