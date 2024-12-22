package days

import arrow.core.flatten
import org.jgrapht.Graph
import arrow.core.memoize
import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.alg.shortestpath.EppsteinShortestPathIterator
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleDirectedGraph
import utils.*

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

fun getKeyPadMovesFromToWorker(keyboard:Grid, from: Point2D, to:Point2D): List<String> {
  val g: Graph<Point2D, DirectionEdge> = SimpleDirectedGraph(DirectionEdge::class.java)
  val dijkstraAlg = DijkstraShortestPath(g)
  keyboard.forEach { g.addVertex(it.key) }
  keyboard.forEach { current ->
    directions4.map { it to current.key.move(it) }. filter { it.second in keyboard }. forEach {
      g.addEdge(current.key, it.second, DirectionEdge(it.first.toKeyPress()))
    }
  }
  val oneShortestPath = dijkstraAlg.getPaths(from).getPath(to) ?: throw Throwable("Cannot find a path")
  return EppsteinShortestPathIterator(g,from,to).asSequence().takeWhile { it.weight == oneShortestPath.weight }.map { it.edgeList.map { it.label }.joinToString("") + "A" }.toList()
}

val getKeyPadMovesFromTo = ::getKeyPadMovesFromToWorker.memoize()

fun minimalPathOf(code:String, nrOfRobots:Int): String {
  fun shortestPathsWorker(keys: String, padType: PAD): List<String> {
    val keyboard = getKeyboard(padType)
    var from = keyboard.getPositionOf("A")
    var shortestPaths = listOf("")
    keys.chunked(1).forEach {
      val to = keyboard.getPositionOf(it)
      shortestPaths = getKeyPadMovesFromTo(keyboard,from,to).flatMap { newPath ->
        shortestPaths.map { oldPath -> "$oldPath$newPath" }
      }
      from = to
    }
    return shortestPaths
  }
  val shortestPaths = ::shortestPathsWorker.memoize()

  var sequences = shortestPaths(code,PAD.NUMPAD)

  repeat(nrOfRobots) {
    sequences = sequences.flatMap { shortestPaths(it,PAD.KEYPAD) }
    println("Robot ${it+1} has ${sequences.size} moves")
  }

  return sequences.minBy { it.length }
}

fun getKeyboard(padType: PAD): Grid = (if (padType==PAD.KEYPAD) kedPad else numPad).toGrid().filter { it.value != "." }
fun complexity(path: String, code:String) = path.length * code.dropLast(1).toLong()

suspend fun main() = AdventOfCode(day = 21, year = 2024) {
  val codes = input.lines()
  part1 = codes.sumOf { complexity(minimalPathOf(it,2),it)  }
}.start()

var example = """
029A
980A
179A
456A
379A
""".trimIndent()