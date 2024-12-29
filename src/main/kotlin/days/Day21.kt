package days

import org.jgrapht.Graph
import arrow.core.memoize
import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.alg.shortestpath.EppsteinShortestPathIterator
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleDirectedGraph
import utils.*
import kotlin.math.min

val numPad = """
  789
  456
  123
  .0A
""".trimIndent()

val kedPad = """
  .^A
  <v>
""".trimIndent()

internal class DirectionEdge(val label: String): DefaultEdge() {
  override fun toString(): String {
    return "($source : $target : $label)"
  }
}
enum class PAD() { NUMPAD, KEYPAD }

fun getKeyPadMovesFromToWorker(from: String, to:String, padType: PAD): List<String> {
  val keyboard = getKeyboard(padType)
  val fromCoordinate = keyboard.getPositionOf(from)
  val toCoordinate = keyboard.getPositionOf(to)
  val g: Graph<Point2D, DirectionEdge> = SimpleDirectedGraph(DirectionEdge::class.java)
  val dijkstraAlg = DijkstraShortestPath(g)

  keyboard.forEach { g.addVertex(it.key) }
  keyboard.forEach { current ->
    directions4.map { it to current.key.move(it) }. filter { it.second in keyboard }. forEach {
      g.addEdge(current.key, it.second, DirectionEdge(it.first.toKeyPress()))
    }
  }
  val oneShortestPath = dijkstraAlg.getPaths(fromCoordinate).getPath(toCoordinate) ?: throw Throwable("Cannot find a path")
  return EppsteinShortestPathIterator(g,fromCoordinate,toCoordinate).asSequence().takeWhile { it.weight == oneShortestPath.weight }.map { it.edgeList.map { it.label }.joinToString("") + "A" }.toList()
}
val getKeyPadMovesFromTo = ::getKeyPadMovesFromToWorker.memoize()

fun shortestPathsWorker(keysToPress: String, padType: PAD): List<String> {
  var from = "A"
  var shortestPaths = listOf("")
  keysToPress.chunked(1).forEach {
    val to = it
    shortestPaths = getKeyPadMovesFromTo(from,to, padType).flatMap { newPath ->
      shortestPaths.map { oldPath -> "$oldPath$newPath" }
    }
    from = to
  }
  return shortestPaths
}
val shortestPaths = ::shortestPathsWorker.memoize()


fun getCostWorker(a:String, b:String, padType: PAD, depth:Int = 0): Long {
    if (depth == 1) {
      return getKeyPadMovesFromTo(a, b, padType).minOf { it.length }.toLong()
    }
    val allShortestPaths = getKeyPadMovesFromTo(a, b ,padType)
    var bestCost = Long.MAX_VALUE
    allShortestPaths.forEach { path ->
        val sequence = "A$path" // we have to start from A
        var cost = 0L
        sequence.chunked(1).zipWithNext().forEach {
          cost += getCost(it.first, it.second, padType, depth-1)
        }
      bestCost = min(bestCost,cost)
    }
  return bestCost
}
val getCost = ::getCostWorker.memoize()

fun minimalPathOf(code:String, depth:Int): Long {
  var sequences = shortestPaths(code,PAD.NUMPAD) // possible sequences on the Numpad
  return sequences.minOf {
    "A$it".chunked(1).zipWithNext().sumOf { getCost(it.first, it.second, PAD.KEYPAD, depth) }
  }
}

fun getKeyboard(padType: PAD): Grid = (if (padType==PAD.KEYPAD) kedPad else numPad).toGrid().filter { it.value != "." }
fun codeToNumber(code:String) = code.dropLast(1).toLong()

suspend fun main() = AdventOfCode(day = 21, year = 2024) {
  val codes = input.lines()
  part1 = codes.sumOf { minimalPathOf(it, 2) * codeToNumber(it) }
  part2 = codes.sumOf { minimalPathOf(it, 25) * codeToNumber(it) }
}.start()
