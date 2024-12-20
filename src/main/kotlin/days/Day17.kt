package days
import utils.*
import kotlin.math.pow

fun executeAll(registers: List<Long>, program: List<Int>): MutableList<Long> {
  var (regA, regB, regC) = registers;

  fun getComboOperand(value: Long): Long {
    return when (value) {
      in (0..3) -> value
      4L -> regA
      5L -> regB
      6L -> regC
      else -> throw Throwable("Not possible")
    }
  }

  fun executeProgram(): MutableList<Long> {
    val output = mutableListOf<Long>();
    var pointer = 0

    fun op0(comboOperand:Long) = (regA / (2.toDouble().pow(comboOperand.toDouble()))).toLong()

    while (pointer < program.size) {
      val opcode = program[pointer]
      val literalOperand = program[pointer+1].toLong()
      val comboOperand = getComboOperand(literalOperand)

      when (opcode) {
        0 -> regA = op0(comboOperand)
        1 -> regB = regB xor literalOperand
        2 -> regB = comboOperand % 8
        3 -> if (regA != 0L) { pointer = literalOperand.toInt(); continue }
        4 -> regB = regB xor regC
        5 -> output.add(comboOperand % 8)
        6 -> regB = op0(comboOperand)
        7 -> regC = op0(comboOperand)
        else -> throw Throwable("Not possible")
      }
      pointer += 2
    }

    return output
  }

  return executeProgram()
}

suspend fun main() = AdventOfCode(day = 17, year = 2024) {
  val (registers, program) = input.split("\n\n").toPair().let {
    it.first.replace("Register [ABC]: ".toRegex(),"").lines().map { it.toLong() } to
    it.second.replace("Program: ","").split(",").map { it.toInt() }
  }

  part1 = executeAll(registers,program).joinToString(",")

  for (a in 0..10_000_000_000L) {
    val modifiedRegisters = listOf(a) + registers.drop(1)
    if (executeAll(modifiedRegisters,program).joinToString(",") == program.joinToString(",")) {
      part2 = a
      break
    }
  }

}.start()

//val example = """
//Register A: 1024
//Register B: 0
//Register C: 0
//
//Program: 0,3,5,4,3,0
//""".trimIndent()