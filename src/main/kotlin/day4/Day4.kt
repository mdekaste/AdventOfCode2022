package day4

import Challenge

fun main(){
    Day4.part1().let(::println)
    Day4.part2().let(::println)
}

object Day4 : Challenge() {
    val parsed = input.lines().map { line ->
        line.split(",").map {
            it.split("-").map(String::toInt).let { (a, b) -> (a..b).toSet() } }
        }
    override fun part1() =  parsed.count { (a, b) -> a.containsAll(b) || b.containsAll(a) }
    override fun part2() =  parsed.count { (a, b) -> a.intersect(b).isNotEmpty() }
}