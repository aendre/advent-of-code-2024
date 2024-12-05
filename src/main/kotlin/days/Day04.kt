package days
import utils.AdventOfCode
import utils.CanvasDirection
import utils.Point2D

private val example = """.M.S......
..A..MSMS.
.M.S.MAA..
..A.ASMSM.
.M.S.M....
..........
S.S.S.S.S.
.A.A.A.A..
M.M.M.M.M.
..........""".trim()

suspend fun day04() = AdventOfCode(day = 4, year = 2024) {
    val matrix = mutableMapOf<Point2D,String>()
    input.lines().forEachIndexed { row, it -> it.chunked(1).forEachIndexed { column, char -> matrix[Point2D(column,row)] = char } }

    part1 = matrix.entries.sumOf { item ->
        CanvasDirection.entries.map { dir ->
            (0..<4)
                .map { item.key.stepTo(dir.value, it) }
                .map { matrix[it] }
                .joinToString("")
        }.count { it == "XMAS" }
    }.toString()

    val xMasDirection = listOf(CanvasDirection.RIGHT_DOWN, CanvasDirection.LEFT_UP, CanvasDirection.RIGHT_UP, CanvasDirection.LEFT_DOWN)
    part2 = matrix.entries.sumOf { item ->
        if (xMasDirection.map { dir ->
            (0..<2)
                .map { item.key.stepTo(dir.value, it) }
                .map { matrix[it] }
                .joinToString("")
        }.chunked(2)
            .map { it.joinToString("") }
            .all { it == "ASAM" || it == "AMAS" }) 1L else 0
    }.toString()
    //2978 - to high

}.start()