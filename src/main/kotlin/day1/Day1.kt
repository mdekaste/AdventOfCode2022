package day1

import Challenge

fun main(){
    println(Day1.part1())
    println(Day1.part2())
}

object Day1 : Challenge() {
    override fun part1(): Any? {
        return input.lines()[0]
    }

    override fun part2(): Any? {
        return input.lines()[1]
    }
}