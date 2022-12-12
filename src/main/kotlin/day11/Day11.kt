package day11

import Challenge
import java.util.stream.Stream
import kotlin.time.measureTime

fun main() {
    val day: Day11
    measureTime {
        day = Day11()
    }.let(::println)
    measureTime {
        day.part1().let(::println)
    }.let(::println)
    measureTime {
        day.part2().let(::println)
    }.let(::println)
}

class Day11() : Challenge() {

    class Monkey(
        val initialItems: List<Long>,
        val inspection: (Long) -> Long,
        val testNumber: Long,
        val ifTrue: Int,
        val ifFalse: Int
    )

    val parsed = input.split(System.lineSeparator() + System.lineSeparator()).map {
        it.lines().drop(1).let { (a, b, c, d, e) ->
            Monkey(
                initialItems = a.substringAfter(": ").split(", ").map(String::toLong),
                inspection = b.substringAfter("old ").split(" ")
                    .let { (type, value) -> (type == "+") to value.toLongOrNull() }
                    .let { (isAdd, long) -> if (isAdd) { x -> x + (long ?: x) } else { x -> x * (long ?: x) } },
                testNumber = c.substringAfter("by ").toLong(),
                ifTrue = d.substringAfter("monkey ").toInt(),
                ifFalse = e.substringAfter("monkey ").toInt()
            )
        }
    }

    private fun solve(times: Int, worryMitigation: (Long) -> Long): Long {
        val inspections = IntArray(parsed.size)
        val itemsToTrack = parsed.map { it.initialItems.toMutableList() }
        repeat(times) {
            for ((index, monkey) in parsed.withIndex()) {
                val items = itemsToTrack[index]
                inspections[index] += items.size
                for (item in items) {
                    val worry = worryMitigation(monkey.inspection(item))
                    val target = if (worry % monkey.testNumber == 0L) monkey.ifTrue else monkey.ifFalse
                    itemsToTrack[target] += worry
                }
                itemsToTrack[index].clear()
            }
        }
        return inspections.sortedDescending().let { (a, b) -> 1L * a * b }
    }

    override fun part1() = solve(20) { it / 3 }

    val reductionNumberPart2 = parsed.map(Monkey::testNumber).reduce(Long::times)
    override fun part2() = solve(10_000) { it % reductionNumberPart2 }
}
