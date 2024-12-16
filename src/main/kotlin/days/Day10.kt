package days

import org.jgrapht.Graph
import org.jgrapht.alg.shortestpath.AllDirectedPaths
import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import utils.*

suspend fun main() = AdventOfCode(day = 10, year = 2024) {
  val g: Graph<Point2D, DefaultEdge> = DefaultDirectedGraph(DefaultEdge::class.java)
  val dijkstraAlg = DijkstraShortestPath(g)
  val topologicalMap = input.toGrid()

  topologicalMap.entries.forEach { g.addVertex(it.key) }
  topologicalMap.entries.forEach { step ->
    directions4.forEach {
      val nextStep = step.key.move(it)
      val valueAtNextStep = topologicalMap.get(nextStep)
      if (valueAtNextStep!=null && valueAtNextStep.toInt()-1==step.value.toInt()) {
        g.addEdge(step.key, nextStep )
      }
    }
  }

  val trailHeads = topologicalMap.entries.filter { it.value == "0" }.map { it.key }
  val trailEnds = topologicalMap.entries.filter { it.value == "9" }.map { it.key }

  part1 = trailHeads.sumOf { head ->
    trailEnds.count {tail -> dijkstraAlg.getPaths(head).getPath(tail) != null }
  }

  val g2 = AllDirectedPaths(g)
  part2 = trailHeads.sumOf { head ->
    trailEnds.sumOf { tail -> g2.getAllPaths(head, tail, true, null).size }
  }
}.start()
