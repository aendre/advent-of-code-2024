package days

import utils.AdventOfCode
import kotlin.math.abs

suspend fun day01() {
    val example = """3   4
    4   3
    2   5
    1   3
    3   9
    3   3"""

    AdventOfCode(day = 1, year = 2024) { input ->
       val pairs = input.lines().map { it.split("   ") }
       val firstList = pairs.map { it[0].toInt() }.sorted();
       val secondList = pairs.map { it[1].toInt() }.sorted();

       val diff = firstList.indices.map { abs(firstList[it] - secondList[it]) }
       println("part I  : ${diff.sum()}")

        val occurrences = firstList.map {
           secondList.count { secondItem -> it == secondItem }
       }
        val part2 = firstList.indices.sumOf { occurrences[it] * firstList[it] }
        println("part II : $part2")
    }.start()
}