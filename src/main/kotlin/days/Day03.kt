package days

import utils.AdventOfCode

private const val example = """xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"""

suspend fun main() = AdventOfCode(day = 3, year = 2024) {
    part1 = Regex("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)")
        .findAll(example).map { it.groupValues[1].toInt() to it.groupValues[2].toInt()}
        .sumOf { it.first * it.second }


   part2 = Regex("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)|do\\(\\)|don't\\(\\)")
        .findAll(input).map { it.groupValues }
        .fold(Pair(true,0)) { acc, match ->
            when (match[0]) {
                "do()" -> acc.copy(first = true)
                "don't()" -> acc.copy(first = false)
                else -> if (acc.first) Pair(true, (acc.second) + (match[1].toInt() * match[2].toInt())) else acc
            }
        }
}.start()
