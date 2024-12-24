package days
import utils.*

suspend fun main() = AdventOfCode(day = 24, year = 2024) {
    val (initialWires, gates) = input.split("\n\n").toPair().let {
        it.first.lines().map { it.split(": ").toPair().let { it.first to it.second.toInt() } } to
        it.second.lines().map { it.replace("-> ","").split(" ") }
    }
    val wires = initialWires.toMap().toMutableMap()

    fun compute(gate:List<String>) {
        val (in1,op,in2,out) = gate
        if (in1 in wires && in2 in wires && out !in wires) {
            wires[out] = when (op) {
                "AND" -> wires[in1]!! and wires[in2]!!
                "XOR" -> wires[in1]!! xor wires[in2]!!
                else -> wires[in1]!! or wires[in2]!!
            }
        }
    }

    fun areAllZsCalculated() = gates.filter { it[3].startsWith("z") }.all { it[3] in wires }

    while (!areAllZsCalculated()) {
        gates.forEach { compute(it) }
    }

    part1 = wires.filter { it.key.startsWith("z") }
        .toList().sortedBy { it.first }.reversed()
        .map { it.second }.joinToString("").toLong(2)


}.start()
