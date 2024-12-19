package days

import org.jgrapht.Graph
import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleDirectedGraph
import utils.*

val MEMORY_SIZE = 70
val FIRST_N_BYTES = 1024

fun buildGraph(memoryMap:Grid): Graph<Point2D, DefaultEdge> {
  val g: Graph<Point2D,DefaultEdge> = SimpleDirectedGraph(DefaultEdge::class.java)
  memoryMap.forEach { g.addVertex(it.key)  }
  memoryMap.forEach { current ->
    directions4.forEach {
      if (memoryMap.containsKey(current.key.move(it))) {
        g.addEdge(current.key, current.key.move(it))
      }
    }
  }
  return g
}

fun getShortestPathSize(bytePositions: List<Point2D>, takeFirst: Int): Int {
  val part1Positions = bytePositions.take(takeFirst)
  val memoryMap:Grid = buildMap {
    for (x in 0..MEMORY_SIZE) {
      for (y in 0..MEMORY_SIZE) {
        val position = Point2D(x,y)
        val content = if (position in part1Positions) "#" else "."
        put(position, content)
      }
    }
  }.filter { it.value != "#" }

  val dijkstraAlg = DijkstraShortestPath(buildGraph(memoryMap))
  val paths = dijkstraAlg.getPaths(Point2D(0,0))
  val shortest = paths.getPath(Point2D(MEMORY_SIZE,MEMORY_SIZE))
  return shortest?.edgeList?.size ?: 0
}

suspend fun main() = AdventOfCode(day = 18, year = 2024) {
  val bytePositions = input.lines().map { it.split(",").map { it.toInt() }.toPair().toPoint2D() }

  part1 = getShortestPathSize(bytePositions, FIRST_N_BYTES)
  var iteration = 0;
  do { iteration++ } while (getShortestPathSize(bytePositions, iteration) > 0)
  part2 = bytePositions[iteration-1]
}.start()