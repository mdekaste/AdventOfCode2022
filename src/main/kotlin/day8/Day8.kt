package day8

import Challenge

fun main() {
    Day8.part1().let(::println)
    Day8.part2().let(::println)
}

object Day8 : Challenge() {
    val GROW_DIRECTIONS = listOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1)
    val parsed = input.lines()
        .withIndex()
        .flatMap { (y, line) -> line.withIndex().map { (x, value) -> (y to x) to value - '0' } }
        .toMap()

    val growths = parsed.map { (key, value) -> growOutwards(key.first, key.second, value) }

    override fun part1() = growths.count { it.any { (canSee, _) -> canSee } }

    override fun part2() = growths.maxOf { growths -> growths.map { it.second }.reduce(Int::times) }

    fun growOutwards(sourceY: Int, sourceX: Int, sourceValue: Int) = GROW_DIRECTIONS
        .map { (yDif, xDif) ->
            generateSequence(sourceY to sourceX) { (y, x) -> y + yDif to x + xDif }
                .withIndex()
                .drop(1)
                .first { !parsed.containsKey(it.value) || parsed.getValue(it.value) >= sourceValue }
                .let { (index, value) -> (!parsed.containsKey(value)).let { it to (index - if (it) 1 else 0) } }
        }
}
