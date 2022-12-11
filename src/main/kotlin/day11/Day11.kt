package day11

import Challenge

fun main() {
    Day11.part1().let(::println)
    Day11.part2().let(::println)
}

object Day11 : Challenge() {
    class Monkey(
        val inspection: (Long) -> Long,
        val testNumber: Long,
        private val ifTrue: Int,
        private val ifFalse: Int
    ) {
        fun throwTarget(number: Long) = if (number % testNumber == 0L) ifTrue else ifFalse
    }

    val parsed = input.split(System.lineSeparator() + System.lineSeparator()).map {
        it.lines().drop(1).let { (a, b, c, d, e) ->
            Monkey(
                inspection = b.substringAfter("old ").split(" ")
                    .let { (type, value) -> (type == "+") to value.toLongOrNull() }
                    .let { (isAdd, long) -> if (isAdd) { x -> x + (long ?: x) } else { x -> x * (long ?: x) } },
                testNumber = c.substringAfter("by ").toLong(),
                ifTrue = d.substringAfter("monkey ").toInt(),
                ifFalse = e.substringAfter("monkey ").toInt()
            ) to a.substringAfter(": ").split(", ").map(String::toLong)
        }
    }.unzip()

    private val monkeys = parsed.first
    private val initialItems = parsed.second

    private fun solve(times: Int, worryMitigation: (Long) -> Long): Long {
        val inspections = LongArray(monkeys.size)
        val itemsToTrack = initialItems.map { it.toMutableList() }
        repeat(times) {
            for ((index, monkey) in monkeys.withIndex()) {
                inspections[index] = inspections[index] + itemsToTrack[index].size
                for (item in itemsToTrack[index]) {
                    val monkeyDoInspection = worryMitigation(monkey.inspection(item))
                    itemsToTrack[monkey.throwTarget(monkeyDoInspection)] += monkeyDoInspection
                }
                itemsToTrack[index].clear()
            }
        }
        return inspections.sortedDescending().let { (a, b) -> a * b }
    }

    override fun part1() = solve(20) { it / 3 }

    override fun part2() = solve(10_000) { it % monkeys.map(Monkey::testNumber).reduce(Long::times) }
}
