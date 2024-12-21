package days

import utils.*

fun getSameNeightbours(garden: Grid, region: List<Point2D>, plantType: String): List<Point2D> {
  val sameNeighbours = region.flatMap { from ->
    directions4.mapNotNull {
      val nextPosition = from.move(it)
      if (garden[nextPosition] == plantType && nextPosition !in region) nextPosition else null
    }
  }

  if (sameNeighbours.isEmpty()) {
    return region.distinct()
  }
  return getSameNeightbours(garden, buildList { addAll(region); addAll(sameNeighbours) }.distinct(), plantType )
}

fun getRegions(garden: Grid): MutableList<List<Point2D>> {
  val visited = mutableSetOf<Point2D>()
  val regions = mutableListOf<List<Point2D>>()
  garden.entries.forEach { plant ->
    if (plant.key !in visited) {
      val region = getSameNeightbours(garden, mutableListOf(plant.key), plant.value)
      region.forEach { visited.add(it) }
      regions.add(region)
    }
  }
  return regions
}

fun perimeter(region: List<Point2D>) : Long {
  return region.sumOf {current ->
    directions4.count {
      current.move(it) !in region
    }.toLong()
  }
}

fun sides(region: List<Point2D>) : Int {
  fun countRepeatingPositions(region:Grid): Int {
    val walls = region.getRows().flatMap {
      it.filter { it.key.move(CanvasDirection.LEFT) !in region }.toList().map { it.first }
    }

    return walls.toGrid().getColumns().filter { it.isNotEmpty() }.map { columnGrid ->
     columnGrid.entries.map { it.key }.count {
        it.move(CanvasDirection.DOWN) !in columnGrid
      }
    }.sum()
  }

  val leftSide = region.toGrid()
  val bottomSide = leftSide.rotate90()
  val rightSide = bottomSide.rotate90()
  val topSide = rightSide.rotate90()
  return countRepeatingPositions(leftSide) + countRepeatingPositions(bottomSide) + countRepeatingPositions(rightSide) + countRepeatingPositions(topSide)
}

suspend fun main() = AdventOfCode(day = 12, year = 2024) {
  val garden = input.toGrid()
  val regions = getRegions(garden)

  part1 = regions.sumOf { perimeter(it) * it.count() }
  part2 = regions.sumOf { sides(it) * it.count()  }
}.start()