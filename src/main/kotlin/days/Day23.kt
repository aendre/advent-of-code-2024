package days
import org.jgrapht.Graph
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import arrow.core.memoize
import utils.*

suspend fun main() = AdventOfCode(day = 23, year = 2024) {
  val pairs = input.lines().map { it.split("-").toPair() }
  val g: Graph<String, DefaultEdge> = DefaultDirectedGraph(DefaultEdge::class.java)
  pairs.forEach {
    g.addVertex(it.first)
    g.addVertex(it.second)
    g.addEdge(it.first,it.second)
    g.addEdge(it.second,it.first)
  }

  fun connectedVerticesToWorker(vertex: String) = g.edgesOf(vertex).flatMap { listOf(g.getEdgeSource(it),g.getEdgeTarget(it)) }.distinct().filter { it != vertex }
  val connectedVerticesTo = ::connectedVerticesToWorker.memoize()

  val sets = mutableSetOf<List<String>>()
  fun search(node: String, interconnected: List<String>) {
    val key = interconnected.sorted()
    if (key in sets) return
    sets.add(key)
    connectedVerticesTo(node).forEach { neighbour ->
      val isComplete = interconnected.all { neighbour in connectedVerticesTo(it) }
      val notAlreadyIn = neighbour !in interconnected
      if (notAlreadyIn && isComplete) {
        search(neighbour, interconnected + listOf(neighbour))
      }
    }
  }

  g.vertexSet().toList().forEach { search(it, listOf(it)) }
  part1 = sets.count { it.size == 3 && it.any { it.startsWith("t") }}
  part2 = sets.maxBy { it.size }.joinToString(",")

}.start()