package day5

import Challenge

fun main(){
    Day5.part1().let(::println)
    Day5.part2().let(::println)
}

object Day5 : Challenge() {
    val regex = """move (\d+) from (\d+) to (\d+)""".toRegex()
    val parsed = input.split(System.lineSeparator() + System.lineSeparator()).let { (a,b) ->
        List(9){ mutableListOf<Char>() }.apply {
            a.lines().forEach { line ->
                for((index, value) in line.withIndex()){
                    if(value.isUpperCase()){
                        this[(index - 1) / 4].add(0, value)
                    }
                }
            }
        } to b.lines().mapNotNull { line ->
            regex.matchEntire(line)?.destructured?.toList()?.map(String::toInt)
        }
    }

    override fun part1() = craneGame(parsed.first.map(List<Char>::toMutableList), parsed.second, false)

    override fun part2() = craneGame(parsed.first.map(List<Char>::toMutableList), parsed.second, true)

    private fun craneGame(stacks: List<MutableList<Char>>, moves: List<List<Int>>, reverse: Boolean): String {
        moves.forEach { (amount, from, to) ->
            stacks[to - 1].addAll(
                grabTop(stacks[from - 1], amount, reverse)
            )
        }
        return stacks.map(List<*>::last).joinToString(separator = "")
    }
    private fun grabTop(toRemoveFrom: MutableList<Char>, amount: Int, reverse: Boolean) =
        MutableList(amount){ toRemoveFrom.removeLast() }
            .apply { if(reverse) reverse() }
}