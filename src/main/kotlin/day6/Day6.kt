package day6

import Challenge

fun main(){
    Day6.part1().let(::println)
    Day6.part2().let(::println)
}

object Day6 : Challenge() {
    override fun part1() = solve(4)
    override fun part2() = solve(14)
    private fun solve(size: Int) = input.windowed(size).indexOfFirst { it.toSet().size == size } + size
}

