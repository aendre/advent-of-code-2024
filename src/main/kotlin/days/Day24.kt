package days
import utils.*

fun toDotFile(wires:Map<String,Int>, gates: List<List<String>>) {
    var out = """
digraph g {
  fontname="Helvetica,Arial,sans-serif"
  node [fontname="Helvetica,Arial,sans-serif"]
  edge [fontname="Helvetica,Arial,sans-serif" fontsize="8"]
""";
    wires.forEach {
        out += "  node [fillcolor=\"white\" style=\"filled\"] ${it.key}; \n"
    }
    gates.forEach {
        val (in1,op,in2,outwire) = it
        if (op=="XOR") out += "  node [fillcolor=\"red\" style=\"filled\"] ${outwire}; \n"
        if (op=="AND") out += "  node [fillcolor=\"blue\" style=\"filled\"] ${outwire}; \n"
        if (op=="OR") out += "  node [fillcolor=\"green\" style=\"filled\"] ${outwire}; \n"
    }
    gates.forEach {
        val (in1,op,in2,outwire) = it
        out += "$in1 -> $outwire  [headlabel=$op]\n"
        out += "$in2 -> $outwire  [headlabel=$op]\n"
    }

    out+="\n }"
    writeContent("build/cache/input-24.dot", out)
}

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

    toDotFile(initialWires.toMap(),gates)
    part2 = listOf("z12", "kwb","z24","tgr","z16","qkf","jqn","cph").sorted().joinToString(",")
}.start()