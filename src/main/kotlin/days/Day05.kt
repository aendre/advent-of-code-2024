package days
import utils.AdventOfCode
import utils.toPair
import java.util.*

suspend fun main() = AdventOfCode(day = 5, year = 2024) {
    val (rules, updates) = input.split("\n\n").toPair().let {
        it.first.lines().map { it.split("|").map { it.toInt()}.toPair() } to
                it.second.lines().map { it.split(",").map { it.toInt() } }
    }

    fun middle(list: List<Int>) = list.get(((list.count() - 1) / 2))
    fun toPageIndex(pages: List<Int>, rule: Pair<Int,Int>) = pages.indexOf(rule.first) to  pages.indexOf(rule.second)
    fun isRightOrder(pages: List<Int>) = rules.map { toPageIndex(pages,it) }.all { it.first < it.second || it.first == -1 || it.second == -1 }
    fun orderPages(pages: List<Int>): List<Int> {
        val newPages = pages.toMutableList()
        while (!isRightOrder(newPages)) {
            rules.forEach {
                val (first, second) = toPageIndex(newPages,it)
                if (first != -1 && second != -1 && first > second) {
                    Collections.swap(newPages,first, second)
                }
            }
        }
        return newPages
    }

    part1 = updates.filter(::isRightOrder).sumOf { middle(it) }
    part2 = updates.filter{!isRightOrder(it)}.map(::orderPages).sumOf { middle(it) }
}.start()


private val example = """47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47""".trim()