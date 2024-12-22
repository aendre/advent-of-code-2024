package days
import utils.*
import kotlin.math.pow

fun executeAll(registers: List<Long>, program: List<Long>): MutableList<Long> {
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
        0L -> regA = op0(comboOperand)
        1L -> regB = regB xor literalOperand
        2L -> regB = comboOperand % 8
        3L -> if (regA != 0L) { pointer = literalOperand.toInt(); continue }
        4L -> regB = regB xor regC
        5L -> output.add(comboOperand % 8)
        6L -> regB = op0(comboOperand)
        7L -> regC = op0(comboOperand)
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
    it.second.replace("Program: ","").split(",").map { it.toLong() }
  }

  part1 = executeAll(registers,program).joinToString(",")

  fun solvePart2(program: List<Long>, target: List<Long>): Long {
    var registerA = if (target.size == 1) {
      0
    } else {
      8 * solvePart2(program, target.drop(1))
    }
    while(executeAll(listOf(registerA) + registers.drop(1),program) != target) {
      registerA++
    }
    return registerA
  }

  part2 = solvePart2(program,program)

}.start()