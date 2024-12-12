package days

import arrow.core.memoize
import utils.*

fun engrave(stone: String):List<String> {
  if (stone == "0") return listOf("1")
  if (stone.count() % 2 == 0) {
    return stone.chunked(stone.count()/2).map { it.replace(Regex("^0*"),"")}.map { it.ifEmpty { "0" } }
  }
  return listOf((stone.toLong() * 2024).toString())
}
val memoizedEngrave = ::engrave.memoize()

fun solveFastWorker (iteration: Int, stone:String): Long {
  if (iteration == 0) return 0

  val newStones = memoizedEngrave(stone)
  val increase = if (newStones.count()>1) 1 else 0
  return increase + newStones.sumOf { solveFast(iteration-1, it) }
}

val solveFast = ::solveFastWorker.memoize()

fun solveSlow(blinks: Int, initialList: List<String>): Long {
  var stones = initialList.toList()
  repeat(blinks) {
    stones = stones.flatMap {
      engrave(it)
    }
  }
  return stones.count().toLong()
}

suspend fun main() = AdventOfCode(day = 11, year = 2024) {
  val stones = input.split(" ")

  part1 = solveSlow(25, stones)
  part2 = stones.sumOf { solveFast(75, it) } + stones.count()
}.start()