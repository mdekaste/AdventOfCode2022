package day3

import Challenge

fun main() {
    println(Day3.part1())
    println(Day3.part2())
}

object Day3 : Challenge() {
    val parsed = input.lines()
    private fun valueExtract(char: Char) = if (char.isLowerCase()) char - 'a' + 1 else char - 'A' + 27
    override fun part1() = parsed
        .map { it.chunked(it.length / 2) }
        .map { (a, b) -> a.first(b::contains) }
        .sumOf(::valueExtract)
    override fun part2() = parsed.chunked(3)
        .map { (a, b, c) -> a.first { it in b && it in c } }
        .sumOf(::valueExtract)
}
