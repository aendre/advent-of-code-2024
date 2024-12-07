package days
import utils.*

data class Guard(val pos: Point2D, val direction: CanvasDirection)

fun rotate90(dir: CanvasDirection) = when (dir) {
    CanvasDirection.UP -> CanvasDirection.RIGHT
    CanvasDirection.RIGHT -> CanvasDirection.DOWN
    CanvasDirection.DOWN -> CanvasDirection.LEFT
    CanvasDirection.LEFT -> CanvasDirection.UP
    else -> dir
}

fun step(map:Map<Point2D, String>, guard: Guard): Guard {
    val nextPosition = guard.pos.stepTo(guard.direction.value);
    return when (map.get(nextPosition)) {
        "#" -> guard.copy(direction = rotate90(guard.direction))
        else -> guard.copy(pos = nextPosition)
    }
}

fun isInfiniteMap(map:Map<Point2D, String>, g: Guard): Boolean {
    val tolerance = 1000;
    var guard = g.copy();
    val visited = mutableSetOf(guard.pos)
    var lastVisitedSize = visited.size;
    var counter = 0
    while (map.get(guard.pos) != null) {
        guard = step(map,guard)
        visited.add(guard.pos)
        if (visited.size == lastVisitedSize) {
            counter += 1
            if (counter >= tolerance) {
                return true;
            }
        } else {
            counter = 0
            lastVisitedSize = visited.size
        }
    }
    return false
}

fun solvePart1(map:Map<Point2D, String>, g: Guard): Int {
    var guard = g.copy();
    val visited = mutableSetOf(guard.pos)
    while (map.get(guard.pos) != null) {
        guard = step(map,guard)
        visited.add(guard.pos)
    }

    return visited.size - 1
}

suspend fun main() = AdventOfCode(day = 6, year = 2024) {
    val map = toCharMatrix(input)
    val guard = Guard(map.filter { it.value == "^" }.entries.first().key, CanvasDirection.UP)

    part1 = solvePart1(map,guard)
    part2 = map.entries.filter { it.value == "." }.count {
        val newMap = map.toMap().toMutableMap()
        newMap.set(it.key,"#")
        isInfiniteMap(newMap,guard)
    }
}.start()


private val example = """
....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...
""".trim()