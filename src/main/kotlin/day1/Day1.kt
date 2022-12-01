package day1

import Challenge
import java.lang.System.lineSeparator

fun main(){
    println(Day1.part1())
    println(Day1.part2())
}

object Day1 : Challenge() {
    val parsed = input.split(lineSeparator() + lineSeparator())
        .map { it.lines().sumOf(Integer::parseInt) }
        .sortedDescending()
    override fun part1() = parsed.first()

    override fun part2() = parsed.take(3).sum()
}