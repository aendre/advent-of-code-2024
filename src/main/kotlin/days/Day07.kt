package days
import utils.AdventOfCode
import utils.toPair

private val example = """190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20""".trim()

fun combineOperators(operators: List<String>, size:Int, array: List<List<String>> = listOf(List(size) { "" })): List<List<String>> {
   if (size == 0) return array

   return combineOperators(operators, size -1, array.map {
      item -> operators.map { op ->
         val newList = item.toMutableList()
         newList[size-1] = op
         newList
      }
   }.flatten())
}

fun calculate(numbers: List<Long>, operators: List<String>): Long {
   var mutableNumbers = numbers.toMutableList();

   operators.forEach {op ->
      val result = mutableNumbers.take(2).toPair().let {
         when (op) {
            "*" -> it.first * it.second
            "+" -> it.first + it.second
            "||" -> (it.first.toString() + it.second.toString()).toLong()
            else -> { 0}
         }
      }

      mutableNumbers = mutableNumbers.drop(1).toMutableList()
      mutableNumbers[0] = result
   }

   return mutableNumbers[0]
}

suspend fun main() = AdventOfCode(day = 7, year = 2024) {
   val equations = input.lines()
      .map { it.split(": ").toPair() }
      .map { it.first.toLong() to it.second.split(" ").map { op -> op.toLong()  } }

   fun solve(operators: List<String>): Long {
      return equations.sumOf { pair ->
         val possibleOperators = combineOperators(operators,pair.second.count() - 1)
         if (possibleOperators.any { calculate(pair.second, it) == pair.first }) pair.first else 0
      }
   }

   part1 = solve(listOf("*", "+"))
   part2 = solve(listOf("*", "+", "||"))
}.start()
