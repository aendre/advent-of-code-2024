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

fun initializeMovement(region: List<Point2D>): Pair<Point2D,CanvasDirection> {
  val validPositions = listOf(
    CanvasDirection.UP to CanvasDirection.RIGHT,
    CanvasDirection.RIGHT to CanvasDirection.DOWN,
    CanvasDirection.DOWN to CanvasDirection.LEFT,
    CanvasDirection.LEFT to CanvasDirection.UP,
  )

  fun getDirection(region: List<Point2D>, position: Point2D): CanvasDirection {
    return validPositions.find {
      position.move(it.first) !in region && position.move(it.second) in region
    }?.second ?: CanvasDirection.STAY_STILL
  }

  val startingPosition = region.find { getDirection(region,it) != CanvasDirection.STAY_STILL } ?: throw Throwable("No valid starting point in region")
  return startingPosition to getDirection(region,startingPosition)
}

fun sides(region: List<Point2D>) : Int {
  if (region.count() == 1) return 4;

  val (initPosition, initDirection) = initializeMovement(region)
  var nextPosition = initPosition
  var nextDirection = initDirection
  var nrOfTurns = 0
  do {
//    print(nextPosition)
    val newPosition = nextPosition.move(nextDirection);
    if (newPosition !in region) {
      nextDirection = nextDirection.rotate90()
      nrOfTurns+=1
//      print("turning at ${nextPosition}.")
    }
    else {
      nextPosition = newPosition
    }
  } while (nextDirection!=initDirection || nextPosition!=initPosition)

  return nrOfTurns
}

suspend fun main() = AdventOfCode(day = 12, year = 2024) {
  val example  = """
AAAA
BBCD
BBCC
EEEC
""".trimIndent()

  val garden = example.toGrid()
  val regions = getRegions(garden)

  part1 = regions.sumOf { perimeter(it) * it.count() }

  regions.forEach {
    val type = garden.get(it[0]) ?: "."
  }

//  part2 = regions.sumOf { sides(it) * it.count() }
}.start()