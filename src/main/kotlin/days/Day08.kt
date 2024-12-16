package days
import utils.*

fun getAntinodes(map:Grid, antennas: List<Point2D>): List<Point2D> {
   return antennas.map { from ->
      antennas.toMutableList().apply { remove(from) }.map { to ->
         val (diffX, diffY) = (from.x - to.x) to (from.y - to.y)
         Point2D(from.x + diffX, from.y + diffY)
      }
   }.flatten().filter { map[it]!=null }
}

fun getResonantAntinodes(map:Grid, antennas: List<Point2D>): List<Point2D> {
   return antennas.flatMap { from ->
      antennas.toMutableList().apply { remove(from) }.flatMap { to ->
         val (diffX, diffY) = (from.x - to.x) to (from.y - to.y)
         var iteration = 1
         var current: Point2D = from.copy()
         val antinodeSeries = mutableListOf<Point2D>()
         while (map[current]!=null ) {
            antinodeSeries.add(current)
            current = Point2D(from.x + (diffX*iteration), from.y + (diffY*iteration))
            iteration += 1
         }
         antinodeSeries
      }
   }
}

suspend fun main() = AdventOfCode(day = 8, year = 2024) {
   val map = input.toGrid()
   val antennas = map.entries.filter { it.value != "." }.groupBy { it.value }

   part1 = antennas.entries.flatMap { getAntinodes(map, it.value.map { it.key }) }.distinct().count()
   part2 = antennas.entries.flatMap { getResonantAntinodes(map, it.value.map { it.key }) }.distinct().count()
}.start()
