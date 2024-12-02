package days

import utils.AdventOfCode
import kotlin.math.abs

suspend fun day02() {
val example = """
7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9
""".trim()

    fun isSafeReport(levels: List<Int>): Int {
        val diff = levels[1] - levels[0]
        if (diff==0) return 0

        val sign = diff / abs(diff);
        val validDiffs = (1..3).map { it*sign}
        for (index in 1..<levels.count()) {
            if (!validDiffs.contains(levels[index] - levels[index-1])) return 0
        }
        return 1
    }

    AdventOfCode(day = 2, year = 2024) { input ->
        val reports = input.lines().map { it.split(" ").map { it.toInt() } }
        reports.map { isSafeReport(it) }.sumOf { it } to

        reports.map { report ->
            isSafeReport(report) + report.indices
                .map { index -> report.filterIndexed { i, item -> i!=index } }
                .map { isSafeReport(it) }
                .sumOf { it }
        }.sumOf { if (it>0) 1L else 0 }
    }.start()
}