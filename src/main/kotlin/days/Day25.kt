package days
import utils.*

suspend fun main() = AdventOfCode(day = 25, year = 2024) {
    val series = input.split("\n\n")
        .map {it.lines().take(1) to it.lines().drop(1).dropLast(1).joinToString("\n") }
        .map { it.first.joinToString("") to it.second.toGrid().getColumns().map { it.entries.count { it.value == "#" } } }

    val locks = series.filter { it.first.chunked(1).all { it =="#"} }.map { it.second }
    val keys = series.filter { it.first.chunked(1).all { it !="#" } }.map { it.second }

    part1 = locks.map { lock -> keys.count { lock.zip(it).all { it.first + it.second <=5 } } }.sum()
}.start()
