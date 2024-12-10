package days

import org.jgrapht.Graph
import org.jgrapht.alg.shortestpath.AllDirectedPaths
import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import utils.*

private val example = """89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732""".trim()

suspend fun main() = AdventOfCode(day = 10, year = 2024) {
  val g: Graph<Point2D, DefaultEdge> = DefaultDirectedGraph(DefaultEdge::class.java)
  val dijkstraAlg = DijkstraShortestPath(g)
  val topologicalMap = toCharMatrix(input)

  topologicalMap.entries.forEach { g.addVertex(it.key) }
  topologicalMap.entries.forEach { step ->
    listOf(CanvasDirection.UP, CanvasDirection.DOWN, CanvasDirection.LEFT, CanvasDirection.RIGHT).forEach {
      val nextStep = step.key.stepTo(it.value)
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
