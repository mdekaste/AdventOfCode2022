package day5

import Challenge

fun main(){
    Day5.part1().let(::println)
    Day5.part2().let(::println)
}

object Day5 : Challenge() {
    val stacks: List<List<Char>>
    val moves: List<List<Int>>
    init {
        val regex = """move (\d+) from (\d+) to (\d+)""".toRegex()
        input.split(System.lineSeparator() + System.lineSeparator()).let { (a,b) ->
            stacks = List(9){ mutableListOf<Char>() }.apply {
                a.lines().forEach { line ->
                    for((index, value) in line.withIndex()){
                        if(value.isUpperCase()){
                            this[(index - 1) / 4].add(0, value)
                        }
                    }
                }
            }
            moves = b.lines().mapNotNull { line ->
                regex.matchEntire(line)?.destructured?.toList()?.map(String::toInt)
            }
        }
    }

    override fun part1() = craneGame(reverse = false)

    override fun part2() = craneGame(reverse = true)

    private fun craneGame(reverse: Boolean): String {
        val stacks = stacks.map(List<Char>::toMutableList)
        for((amount, from, to) in moves){
            stacks[to - 1].addAll(
                grabTop(
                    toRemoveFrom = stacks[from - 1],
                    amount = amount,
                    reverse = reverse
                )
            )
        }
        return stacks.map(List<*>::last).joinToString(separator = "")
    }
    private fun grabTop(toRemoveFrom: MutableList<Char>, amount: Int, reverse: Boolean) =
        MutableList(amount){ toRemoveFrom.removeLast() }
            .apply { if(reverse) reverse() }
}