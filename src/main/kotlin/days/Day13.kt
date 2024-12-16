package days
import arrow.core.split
import utils.*
import kotlin.math.roundToLong

suspend fun main() = AdventOfCode(day = 13, year = 2024) {

  fun solve (eq: List<Pair<Double,Double>>, part2:Boolean = false): Long {
    fun toClawPulls(x:Double) = if (x.roundToLong().toDouble() == x) x.toLong() else 0L
    val (ax,ay) = eq[0]
    val (bx,by) = eq[1]
    var (x,y) = eq[2]
    if (part2) {
      x += 10000000000000
      y += 10000000000000
    }

    val b = (y*ax - ay*x) / (by*ax - bx*ay)
    val a = (x - b*bx) / ax
    val ac =  toClawPulls(a)
    val bc =  toClawPulls(b)

    return if (ac!=0L && bc!=0L) ac*3 + bc else 0
  }

  val equations = input.split("\n\n").map { eq ->
    eq.lines().map {
      it.split(": ").toPair().second
      .replace("X+","")
      .replace("Y+","")
      .replace("X=","")
      .replace("Y=","")
        .split(", ").map { it.toDouble() }.toPair()
    }
  }

  part1 = equations.sumOf { solve(it) }
  part2 = equations.sumOf { solve(it,true) }
}.start()