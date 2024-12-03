package days

import utils.AdventOfCode
import kotlin.math.abs

suspend fun day03() {
val example = """xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"""

    AdventOfCode(day = 3, year = 2024) { input ->
       Regex("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)")
           .findAll(example).map { it.groupValues[1].toInt() to it.groupValues[2].toInt()}
           .sumOf { it.first * it.second }

       Regex("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)|do\\(\\)|don't\\(\\)")
            .findAll(input).map { it.groupValues }
            .fold(Pair(true,0)) { acc, match ->
                when (match[0]) {
                    "do()" -> Pair(true,acc.second)
                    "don't()" -> Pair(false,acc.second)
                    else -> if (acc.first) Pair(true, (acc.second) + (match[1].toInt() * match[2].toInt())) else acc
                }
            }
    }.start()
}