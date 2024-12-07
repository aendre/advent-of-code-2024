package utils

data class Point2D(val x:Int, val y:Int) {
    fun stepTo(dir:Point2D, nrOfSteps: Int = 1): Point2D {
        return Point2D(this.x + dir.x * nrOfSteps, this.y + dir.y * nrOfSteps)
    }
}

// Define directions on a canvas, where origo (0,0) is in the top left corner
enum class CanvasDirection(val value:Point2D) {
    UP (Point2D(0,-1)),
    DOWN (Point2D(0,1)),
    LEFT (Point2D(-1,0)),
    RIGHT (Point2D(1,0)),
    RIGHT_UP (CanvasDirection.RIGHT.value.stepTo(CanvasDirection.UP.value)),
    RIGHT_DOWN (CanvasDirection.RIGHT.value.stepTo(CanvasDirection.DOWN.value)),
    LEFT_UP (CanvasDirection.LEFT.value.stepTo(CanvasDirection.UP.value)),
    LEFT_DOWN (CanvasDirection.LEFT.value.stepTo(CanvasDirection.DOWN.value)),
}

fun toCharMatrix(input: String): Map<Point2D, String> {
    val matrix = mutableMapOf<Point2D,String>()
    input.lines().forEachIndexed { row, it -> it.chunked(1).forEachIndexed { column, char -> matrix[Point2D(column,row)] = char } }
    return matrix.toMap()
}
