package days

import utils.*

fun Long.mix(value:Long) = this xor value
fun Long.prune() = mod(16_777_216L)

fun generateNewSecret(secret:Long) : Long {
  return secret.mix(secret * 64).prune()
      .let { it.mix((it / 32)).prune() }
      .let { it.mix(it * 2048L).prune() }
}

fun secretsOf(seed:Long, times: Int): List<Long> {
  var n = seed;
  return (0..times).map { n = if (it==0) seed else generateNewSecret(n); n }
}

suspend fun main() = AdventOfCode(day = 22, year = 2024) {
  val seeds = input.lines().map { it.toLong() }

  part1 = seeds.sumOf { secretsOf(it,2000).last() }

  val bestBananas = mutableMapOf<String,Long>()
  seeds.forEach {
    val visited = mutableSetOf<String>()
    secretsOf(it, 2000)
      .map { it.toString().takeLast(1).toLong() } // get last digit
      .windowed(size = 5) {
        val sequence = it.zipWithNext().map { it.second - it.first }.joinToString(",")
        val nrOfBananas = it.last()
        if (sequence !in visited) {
          bestBananas.computeIfPresent(sequence) { key, value ->  value + nrOfBananas  }
          bestBananas.putIfAbsent(sequence,nrOfBananas);
          visited.add(sequence)
        }
      }
  }
  part2 = bestBananas.maxBy { it.value }
}.start()