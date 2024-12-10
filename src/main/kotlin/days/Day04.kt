package days
import utils.AdventOfCode
import utils.CanvasDirection
import utils.toCharMatrix

suspend fun main() = AdventOfCode(day = 4, year = 2024) {
    val matrix = toCharMatrix(input)

    part1 = matrix.entries.sumOf { item ->
        CanvasDirection.entries.map { dir ->
            (0..<4)
                .map { item.key.stepTo(dir.value, it) }
                .map { matrix[it] }
                .joinToString("")
        }.count { it == "XMAS" }
    }

    val xMasDirection = listOf(CanvasDirection.RIGHT_DOWN, CanvasDirection.LEFT_UP, CanvasDirection.RIGHT_UP, CanvasDirection.LEFT_DOWN)
    part2 = matrix.entries.sumOf { item ->
        if (xMasDirection.map { dir ->
            (0..<2)
                .map { item.key.stepTo(dir.value, it) }
                .map { matrix[it] }
                .joinToString("")
        }.chunked(2)
            .map { it.joinToString("") }
            .all { it == "ASAM" || it == "AMAS" }) 1.toInt() else 0
    }
    //2978 - to high

}.start()