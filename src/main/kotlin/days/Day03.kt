package days

import utils.AdventOfCode

suspend fun main() = AdventOfCode(day = 3, year = 2024) {
    part1 = Regex("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)")
        .findAll(input).map { it.groupValues[1].toInt() to it.groupValues[2].toInt()}
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
