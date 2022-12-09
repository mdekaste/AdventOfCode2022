package day8

import java.io.File
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

fun main(){
    measureTime {
        Day8Measurable().run {
            part1().let(::println)
            part2().let(::println)
        }
    }.let(::println)
}

class Day8Measurable {
    companion object{
        val input = File(javaClass.getResource("aoc_2022_day08_sparse.txt").path).readText()
        val GROW_DIRECTIONS = listOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1)
    }

    val parsed = input.lines()
        .withIndex()
        .flatMap { (y, line) -> line.withIndex().map { (x, value) -> (y to x) to value - '0' } }
        .toMap()

    val growths = parsed.map { (key, value) -> growOutwards(key.first, key.second, value) }

    fun part1() = growths.count { it.any { (canSee, _) -> canSee } }

    fun part2() = growths.maxOf { growths -> growths.map { it.second }.reduce(Int::times) }

    fun growOutwards(sourceY: Int, sourceX: Int, sourceValue: Int) = GROW_DIRECTIONS
        .map { (yDif, xDif) ->
            generateSequence(sourceY to sourceX) { (y, x) -> y + yDif to x + xDif }
                .withIndex()
                .drop(1)
                .first { !parsed.containsKey(it.value) || parsed.getValue(it.value) >= sourceValue }
                .let { (index, value) -> (!parsed.containsKey(value)).let { it to (index - if (it) 1 else 0) } }
        }
}