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

    AdventOfCode(day = 1, year = 2024) { input ->
       val (a, b) = input.lines().map {
           it.split("   ").map { nr -> nr.toInt()}.toPair()
       }.unzip()
       val first = a.sorted();
       val second = b.sorted();
       val diff = first.zip(second).map { abs(it.first - it.second) }
       println("part I  : ${diff.sum()}")

       val occurrences = first.map {
           second.count { secondItem -> it == secondItem }
       }
       val part2 = first.indices.sumOf { occurrences[it] * first[it] }
           .also(::println)
       println("part II : $part2")
    }.start()
}