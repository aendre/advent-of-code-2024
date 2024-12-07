package days
import utils.*

private val example = """
............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............
""".trim()

fun getAntinodes(map:Map<Point2D, String>, antennas: List<Point2D>): List<Point2D> {
   return antennas.map { from ->
      antennas.toMutableList().apply { remove(from) }.map { to ->
         val (diffX, diffY) = (from.x - to.x) to (from.y - to.y)
         Point2D(from.x + diffX, from.y + diffY)
      }
   }.flatten().filter { map[it]!=null }
}

fun getResonantAntinodes(map:Map<Point2D, String>, antennas: List<Point2D>): List<Point2D> {
   return antennas.map { from ->
      antennas.toMutableList().apply { remove(from) }.map { to ->
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
      }.flatten()
   }.flatten()
}

suspend fun main() = AdventOfCode(day = 8, year = 2024) {
   val map = toCharMatrix(input)
   val antennas = map.entries.filter { it.value != "." }.groupBy { it.value }

   part1 = antennas.entries.map { getAntinodes(map, it.value.map { it.key }) }.flatten().distinct().count()
   part2 = antennas.entries.map { getResonantAntinodes(map, it.value.map { it.key }) }.flatten().distinct().count()
}.start()
