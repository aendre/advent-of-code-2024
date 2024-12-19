package days

import utils.*

fun getMovablePoints(map:Grid, direction: CanvasDirection): List<Pair<Point2D,String>> {
  val lanternfish = map.entries.find { it.value == "@"}?.key ?: throw Throwable("Cannot find the Lanternfish");
  val movablePoints = mutableListOf<Pair<Point2D,String>>()
  var nextBox = lanternfish
  do  {
    movablePoints.add(nextBox to map.getValue(nextBox))
    nextBox = nextBox.move(direction)
  } while (map.getValue(nextBox) == "O" || map.getValue(nextBox) == "[" || map.getValue(nextBox) == "]")
  return movablePoints
}

fun move(map:Grid, direction: CanvasDirection, gatherMovablePoints: (map:Grid, direction:CanvasDirection)-> List<Pair<Point2D,String>>) : Grid {
  val newMap = map.toMutableMap()
  val movablePoints = gatherMovablePoints(map,direction)
  var valuesToCheck = listOf(movablePoints.last().first).toMutableList()

  if (direction==CanvasDirection.UP || direction==CanvasDirection.DOWN) {
    val fromY = movablePoints.minOf { it.first.y }
    val toY = movablePoints.maxOf { it.first.y }
    val range = if (direction==CanvasDirection.DOWN) (fromY..toY) else (toY downTo fromY)
    valuesToCheck = mutableListOf()
    for (y in range) {
      val points = movablePoints.filter { it.first.y == y }.filter { map.getValue(it.first.move(direction)) != "[" && map.getValue(it.first.move(direction)) != "]" }.map { it.first }
      valuesToCheck.addAll(points)
    }
  }

  if (valuesToCheck.all { map.getValue(it.move(direction)) == "." }) {
    movablePoints.forEach { newMap.put(it.first, ".") } // move each point further
    movablePoints.forEach { newMap.put(it.first.move(direction), it.second) } // move each point further
    return newMap
  }
  return map
}

fun score(map:Grid) = map.filter { it.value == "O" || it.value == "[" }.entries.map { it.key }.sumOf { it.x + it.y * 100 }

fun part2Map(original:String) = original
  .replace("#","a")
  .replace(".","b")
  .replace("O","c")
  .replace("a","##")
  .replace("b","..")
  .replace("c","[]")
  .replace("@","@.")

fun getMovableBigBoxes(map:Grid, direction: CanvasDirection): List<Pair<Point2D,String>> {
  if (direction==CanvasDirection.LEFT || direction == CanvasDirection.RIGHT) {
    return getMovablePoints(map,direction)
  }

  val lanternfish = map.entries.find { it.value == "@"}?.key ?: throw Throwable("Cannot find the Lanternfish");
  return getVerticalBoxes(map, direction, listOf(listOf(lanternfish to "@"))).flatten()
}

fun getVerticalBoxes(map: Grid, direction: CanvasDirection, acc: List<List<Pair<Point2D,String>>>) : List<List<Pair<Point2D,String>>> {
  var nextRow = acc.last().map { it.first.move(direction) }.map { it to map.getValue(it) }.filter { it.second !="." }.toMutableList()
  if (nextRow.any { it.second == "#" } || nextRow.all { it.second == "." }) return acc

  if (nextRow.first().second == "]") nextRow = (listOf(nextRow.first().first.move(CanvasDirection.LEFT) to "[") + nextRow).toMutableList()
  if (nextRow.last().second == "[") nextRow.add(nextRow.last().first.move(CanvasDirection.RIGHT) to "]")
  return getVerticalBoxes(map, direction, buildList {
    addAll(acc);
    add(nextRow)
  })
}

suspend fun main() = AdventOfCode(day = 15, year = 2024) {
  var (mapRaw,movesRaw) = input.split("\n\n").toPair()
  var map = mapRaw.toGrid()
  val moves = movesRaw.lines().joinToString("").chunked(1).map { it.toCanvasDirection() }

  moves.forEach { map = move(map, it, ::getMovablePoints) }
  part1 = score(map)

  var map2 = part2Map(mapRaw).toGrid()
  moves.forEach { map2 = move(map2, it, ::getMovableBigBoxes) }
  part2 = score(map2)
}.start()