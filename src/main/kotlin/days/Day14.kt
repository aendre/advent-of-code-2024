package days

import utils.*
import kotlin.math.max

data class Robot(var position:Point2D, val velocity:Point2D)
val NR_OF_TILES_ON_X = 101;
val NR_OF_TILES_ON_Y = 103;

fun Point2D.overflowMove(amount: Point2D): Point2D {
  fun overflowAdd(max:Int, a:Int, b:Int): Int {
    val sum = a + b.rem(max);
    return when {
      sum < 0 -> max + sum
      sum >= max -> sum - max
      else -> sum
    }
  }
  val x = overflowAdd(NR_OF_TILES_ON_X, this.x, amount.x)
  val y = overflowAdd(NR_OF_TILES_ON_Y, this.y, amount.y)
  return Point2D(x,y)
}

fun safetyFactor(robots: List<Robot>): Int {
  val q1 = robots.count { it.position.x < (NR_OF_TILES_ON_X-1)/2 && it.position.y < (NR_OF_TILES_ON_Y-1)/2 }
  val q2 = robots.count { it.position.x < (NR_OF_TILES_ON_X-1)/2 && it.position.y > (NR_OF_TILES_ON_Y-1)/2 }
  val q3 = robots.count { it.position.x > (NR_OF_TILES_ON_X-1)/2 && it.position.y < (NR_OF_TILES_ON_Y-1)/2 }
  val q4 = robots.count { it.position.x > (NR_OF_TILES_ON_X-1)/2 && it.position.y > (NR_OF_TILES_ON_Y-1)/2 }
  return q1 * q2 * q3 * q4
}

fun entropy(robots: List<Robot>): Int {
  val matrix = robots.map { it.position }.toGrid()
  return matrix.map { robot ->
    directions4.sumOf { if (matrix.get(robot.key.move(it))!=null) 1.toInt() else 0  }
  }.sum()
}

suspend fun main() = AdventOfCode(day = 14, year = 2024) {
  var robots = input.lines().map {
    val (position, velocity) = it.replace("p=","").replace("v=","").split(" ").map {
      val (x,y) = it.split(",").map { it.toInt() }.toPair()
      Point2D(x,y)
    }.toPair()
    Robot(position,velocity)
  }

  repeat(100) {
    robots = robots.map { it.position = it.position.overflowMove(it.velocity); it }
  }
  part1 = safetyFactor(robots)

  var maxEntropy = 0
  var seconds = 100;
  while(maxEntropy<1000){
    robots = robots.map { it.position = it.position.overflowMove(it.velocity); it }
    maxEntropy = max(maxEntropy,entropy(robots))
    seconds++
  }
  robots.map { it.position }.toGrid("▉").print()
  part2 = seconds
}.start()

