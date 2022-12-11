package day10

import Challenge

fun main() {
    Day10.part1().let(::println)
    Day10.part2().let(::println)
}

object Day10 : Challenge() {
    private val parsed = input.lines()
        .flatMap {
            when (val split = it.split(" ")) {
                listOf("noop") -> listOf(0)
                else -> listOf(0, split[1].toInt())
            }
        }.runningFold(1, Int::plus).dropLast(1)

    override fun part1() = (20..220 step 40).sumOf { it * parsed[it - 1] }

    override fun part2() = parsed
        .chunked(40)
        .joinToString("\n") {
            it.withIndex().joinToString("") { (index, value) ->
                if (value in (index - 1)..(index + 1)) "#" else "."
            }
        }
}
