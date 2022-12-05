package day5

import Challenge
import extractInts
import removeLast
import splitOnEmpty

fun main() {
    Day5.part1().let(::println)
    Day5.part2().let(::println)
}

object Day5 : Challenge() {
    val stacks: List<List<Char>>
    val moves: List<List<Int>>
    init {
        input.splitOnEmpty().let { (a, b) ->
            stacks = List(9) { mutableListOf<Char>() }.apply {
                a.lines().forEach { line ->
                    for ((index, value) in line.withIndex()) {
                        if (value.isUpperCase()) {
                            this[(index - 1) / 4].add(0, value)
                        }
                    }
                }
            }
            moves = b.lines().map(String::extractInts)
        }
    }

    override fun part1() = craneGame(reverse = true)

    override fun part2() = craneGame(reverse = false)

    private fun craneGame(reverse: Boolean) = stacks.map(List<Char>::toMutableList).apply {
        for ((amount, from, to) in moves) {
            this[to - 1] += this[from - 1].removeLast(amount).apply { if (reverse) reverse() }
        }
    }.joinToString(separator = "") { it.last().toString() }
}
