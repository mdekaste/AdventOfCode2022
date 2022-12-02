package day2

import Challenge

fun main(){
    println(Day2.part1())
    println(Day2.part2())
}

object Day2 : Challenge("Rock Paper Scissors") {
    val parsed = input.lines()
        .map { it.split(" ").map(String::first) }
        .map { (him, me) -> (him - 'A') to (me - 'X') }
    override fun part1() = parsed.sumOf { (him, me) -> ((me + 4 - him) % 3) * 3 + me + 1 }
    override fun part2() = parsed.sumOf { (him, me) -> (me + 2 + him) % 3 + 1 + me * 3 }
}