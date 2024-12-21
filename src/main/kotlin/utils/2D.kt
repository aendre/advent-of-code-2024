package utils

import java.util.Collections.max
import java.util.Collections.min
import kotlin.math.abs

data class Point2D(val x:Int, val y:Int) {
    fun move(dir:CanvasDirection, nrOfSteps: Int = 1): Point2D {
        return Point2D(this.x + dir.value.x * nrOfSteps, this.y + dir.value.y * nrOfSteps)
    }
    fun move(amount:Point2D): Point2D {
        return Point2D(this.x + amount.x, this.y + amount.y)
    }
    fun manhattan(p:Point2D):Int = abs(this.x - p.x) + abs(this.y - p.y)
}


fun Pair<Int,Int>.toPoint2D() = Point2D(this.first,this.second)

// Define directions on a canvas, where origo (0,0) is in the top left corner
enum class CanvasDirection(val value:Point2D) {
    UP (Point2D(0,-1)),
    DOWN (Point2D(0,1)),
    LEFT (Point2D(-1,0)),
    RIGHT (Point2D(1,0)),
    RIGHT_UP (RIGHT.value.move(UP)),
    RIGHT_DOWN (RIGHT.value.move(DOWN)),
    LEFT_UP (LEFT.value.move(UP)),
    LEFT_DOWN (LEFT.value.move(DOWN)),
    STAY_STILL (Point2D(0,0));

    fun rotate90(): CanvasDirection = when (this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
        RIGHT_UP -> RIGHT_DOWN
        RIGHT_DOWN -> LEFT_DOWN
        LEFT_DOWN -> LEFT_UP
        LEFT_UP -> RIGHT_UP
        else -> this
    }

    fun toKeyPress(): String = when(this) {
        UP -> "^"
        RIGHT -> ">"
        DOWN -> "v"
        LEFT -> "<"
        else -> throw Throwable("Cannot convert direction to keypress")
    }
}

fun String.toCanvasDirection(): CanvasDirection = when (this.lowercase()) {
    "v" -> CanvasDirection.DOWN
    "^" -> CanvasDirection.UP
    "<" -> CanvasDirection.LEFT
    ">" -> CanvasDirection.RIGHT
    else -> CanvasDirection.STAY_STILL
}
val directions4 = listOf(CanvasDirection.UP, CanvasDirection.DOWN, CanvasDirection.LEFT, CanvasDirection.RIGHT)

typealias Grid = Map<Point2D, String>;

fun Grid.print(toTerminal:Boolean = true): String {
    var raw = ""
    for (y in this.minY()..this.maxY()) {
        for (x in this.minX()..this.maxX()) {
            raw += this.getOrDefault(Point2D(x,y),"∙")
            if (toTerminal) print(this.getOrDefault(Point2D(x,y), gray("∙")))
        }
        if (toTerminal)  println()
        raw += "\n"
    }
    val width = this.maxX() - this.minX() + 1
    val height = this.maxY() - this.minY() + 1
    if (toTerminal)  println(gray("Grid printed. [width=$width height=$height]"))
    return raw
}

fun Grid.getRows(): List<Grid> {
    return buildList {
        for (y in this@getRows.minY()..this@getRows.maxY()){
            add(this@getRows.filter { it.key.y == y })
        }
    }
}

fun Grid.getColumns(): List<Grid> {
    return buildList {
        for (x in this@getColumns.minX()..this@getColumns.maxX()){
            add(this@getColumns.filter { it.key.x == x })
        }
    }
}

fun Grid.rotate90(): Grid {
    return buildMap {
        this@rotate90.entries.forEach {
            put(Point2D(this@rotate90.minX() - it.key.y,it.key.x), it.value)
        }
    }
}

fun Grid.minX() = min(this.entries.map { it.key.x })
fun Grid.maxX() = max(this.entries.map { it.key.x })
fun Grid.minY() = min(this.entries.map { it.key.y })
fun Grid.maxY() = max(this.entries.map { it.key.y })
fun Grid.getPositionOf(content:String) = this.filter { it.value == content }.map { it.key }.first()

fun String.toGrid(): Grid {
    val matrix = mutableMapOf<Point2D,String>()
    this.lines().forEachIndexed { row, it -> it.chunked(1).forEachIndexed { column, char -> matrix[Point2D(column,row)] = char } }
    return matrix.toMap()
}

fun List<Point2D>.toGrid(content:String = "█"): Grid {
    return buildMap { this@toGrid.forEach { put(it, content) } }
}

