package days

import org.jgrapht.Graph
import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.graph.DefaultWeightedEdge
import org.jgrapht.graph.SimpleDirectedWeightedGraph
import utils.*

fun buildGraph(route:Grid): Graph<Point2D, DefaultWeightedEdge> {
  val g: Graph<Point2D, DefaultWeightedEdge> = SimpleDirectedWeightedGraph(DefaultWeightedEdge::class.java)
  route.forEach { g.addVertex(it.key) }
  route.forEach { current ->
    directions4.map { current.key.move(it) }.filter { it in route}.forEach { g.addEdge(current.key, it) }
  }
  return g
}

fun getShortestPath(route:Grid): List<Point2D> {
  val g = buildGraph(route)
  val from = route.getPositionOf("S")
  val to = route.getPositionOf("E")

  val dijkstraAlg = DijkstraShortestPath(g)
  val path = dijkstraAlg.getPaths(from).getPath(to) ?: throw Throwable("Cannot find a path")
  return path.edgeList.map { g.getEdgeSource(it) } + path.edgeList.map { g.getEdgeTarget(it) }.last()
}

fun solve(route:List<Point2D>, allowedDuration: Int): Int {
  val cheatingRouteSizes = mutableListOf<Int>()
  route.forEachIndexed { index, position ->
    for (shortcut in index+1..<route.size) {
      val distanceOnRoute = shortcut - index;
      val distanceOnCheat = position.manhattan(route[shortcut])
      if (distanceOnCheat <= allowedDuration && distanceOnCheat<distanceOnRoute) {
        cheatingRouteSizes.add(distanceOnRoute-distanceOnCheat)
      }
    }
  }
  return cheatingRouteSizes.count { it >= 100}
}

suspend fun main() = AdventOfCode(day = 20, year = 2024) {
  val route = input.toGrid().filter { it.value != "#" }
  val originalShortestRoute = getShortestPath(route)

  part1 = solve(originalShortestRoute, 2)
  part2 = solve(originalShortestRoute, 20)
}.start()