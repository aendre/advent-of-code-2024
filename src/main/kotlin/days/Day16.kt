package days

import org.jgrapht.Graph
import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.alg.shortestpath.EppsteinShortestPathIterator
import org.jgrapht.graph.DefaultWeightedEdge
import org.jgrapht.graph.SimpleDirectedWeightedGraph
import utils.*

fun Grid.getPositionFor(content:String) = this.entries.filter { it.value == content }.map { it.key }.first()
typealias Node = Pair<Point2D,CanvasDirection>

suspend fun main() = AdventOfCode(day = 16, year = 2024) {
  val map = input.toGrid()
  val routes = map.entries.filter { it.value != "#" }.map { it.key }.toGrid()
  val start = map.getPositionFor("S")
  val end = map.getPositionFor("E")
  val g: Graph<Node,DefaultWeightedEdge> = SimpleDirectedWeightedGraph(DefaultWeightedEdge::class.java)

  // For each position add a node facing to each of the directions
  routes.forEach { current -> directions4.forEach { g.addVertex(Node(current.key, it)) } }
  routes.forEach { current ->
    // Add turns
    listOf(
      CanvasDirection.UP to CanvasDirection.UP.rotate90(),
      CanvasDirection.DOWN to CanvasDirection.DOWN.rotate90(),
      CanvasDirection.RIGHT to CanvasDirection.RIGHT.rotate90(),
      CanvasDirection.LEFT to CanvasDirection.LEFT.rotate90(),
    ).forEach {
      val edgeTo = g.addEdge(current.key to it.first, current.key to it.second)
      g.setEdgeWeight(edgeTo,1000.toDouble())
      val edgeFrom = g.addEdge(current.key to it.second, current.key to it.first)
      g.setEdgeWeight(edgeFrom,1000.toDouble())
    }

    // Add steps
    directions4.forEach {
      val nextStep = current.key.move(it)
      if (routes.containsKey(nextStep)) {
        val edgeStep = g.addEdge(current.key to it, nextStep to it)
        g.setEdgeWeight(edgeStep,1.toDouble())
      }
    }
  }

  val dijkstraAlg = DijkstraShortestPath(g)
  val startNode = Node(start,CanvasDirection.RIGHT)
  val paths = dijkstraAlg.getPaths(startNode)
  part1 = directions4.minOf {
    val shortestPath = paths.getPath(Node(end,it))
    shortestPath.weight
  }

  val lastDirection = directions4.minBy {paths.getPath(Node(end,it)).weight }
  val allShortestPaths = EppsteinShortestPathIterator(g,startNode,Node(end,lastDirection))
  part2 = allShortestPaths.asSequence().takeWhile { it.weight == part1 }.flatMap {
   it.edgeList.map { g.getEdgeSource(it).first }
  }.distinct().count() + 1

}.start()