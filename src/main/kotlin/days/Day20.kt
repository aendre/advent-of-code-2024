package days

import org.jgrapht.Graph
import org.jgrapht.GraphPath
import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.graph.DefaultWeightedEdge
import org.jgrapht.graph.SimpleDirectedWeightedGraph
import utils.*

typealias RaceGraph = Graph<Point2D, DefaultWeightedEdge>

fun buildGraph(route:Grid): RaceGraph {
  val g: Graph<Point2D, DefaultWeightedEdge> = SimpleDirectedWeightedGraph(DefaultWeightedEdge::class.java)
  route.forEach { g.addVertex(it.key) }
  route.forEach { current ->
    directions4.map { current.key.move(it) }. forEach {
      if (route.get(it)!=null) g.addEdge(current.key, it)
    }
  }
  return g;
}

fun getShortestPath(g:RaceGraph, from: Point2D, to:Point2D): GraphPath<Point2D, DefaultWeightedEdge> {
  val dijkstraAlg = DijkstraShortestPath(g)
  return dijkstraAlg.getPaths(from).getPath(to) ?: throw Throwable("Cannot find a path")
}

fun getRoute(g:RaceGraph, path: GraphPath<Point2D, DefaultWeightedEdge>) : List<Point2D> {
  return path.edgeList.map { g.getEdgeSource(it) } + path.edgeList.map { g.getEdgeTarget(it) }.last()
}

suspend fun main() = AdventOfCode(day = 20, year = 2024) {
  val map = example.toGrid();
  val route = map.filter { it.value != "#" }

  val g = buildGraph(route)
  val start = route.getPositionOf("S")
  val end = route.getPositionOf("E")
  val shortestRouteWithoutCheating = getShortestPath(g,start,end)
  val shortestAsCoordinates = getRoute(g,shortestRouteWithoutCheating)

  val cheatingRouteSizes = mutableListOf<Double>()
  shortestAsCoordinates.forEach { current ->
    directions4.map {
      val first = current.move(it)
      val second = first.move(it)
      if (first !in shortestAsCoordinates && second in shortestAsCoordinates) {
        val cheatingEdge = g.addEdge(current,second)
        g.setEdgeWeight(cheatingEdge,2.toDouble())
        cheatingRouteSizes.add(getShortestPath(g,start,end).weight)
        g.removeEdge(cheatingEdge)
      }
    }
  }

  part1 = cheatingRouteSizes.count { shortestRouteWithoutCheating.weight - it >= 100 }
  shortestAsCoordinates.toGrid().print()

}.start()

var example = """
  ###############
  #...#...#.....#
  #.#.#.#.#.###.#
  #S#...#.#.#...#
  #######.#.#.###
  #######.#.#...#
  #######.#.###.#
  ###..E#...#...#
  ###.#######.###
  #...###...#...#
  #.#####.#.###.#
  #.#...#.#.#...#
  #.#.#.#.#.#.###
  #...#...#...###
  ###############
""".trimIndent()