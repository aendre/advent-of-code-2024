package days

import utils.AdventOfCode
import kotlin.math.abs

fun isSafeReport(levels: List<Int>): Int {
    val diff = levels[1] - levels[0]
    if (diff==0) return 0

    val range = if (diff > 0) 1..3 else -3..-1
    for (index in 1..<levels.count()) {
        if (levels[index] - levels[index-1] !in range) return 0
    }
    return 1
}

suspend fun main() = AdventOfCode(day = 2, year = 2024) {
    val reports = input.lines().map { it.split(" ").map { it.toInt() } }
    part1 = reports.map { isSafeReport(it) }.sumOf { it }

    part2 = reports.map { report ->
        isSafeReport(report) + report.indices
            .map { index -> report.filterIndexed { i, item -> i!=index } }
            .map { isSafeReport(it) }
            .sumOf { it }
    }.sumOf { if (it>0) 1L else 0 }
}.start()