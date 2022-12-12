package day12

import Challenge

fun main() {
    Day12.part1().let(::println)
    Day12.part2().let(::println)
}

typealias Point = Pair<Int, Int>
object Day12 : Challenge() {
    lateinit var startPoint: Point
    lateinit var endPoint: Point
    val parsed = input.lines()
        .withIndex()
        .flatMap { (y, line) ->
            line.withIndex()
                .map { (x, char) ->
                    val point = y to x
                    when (char) {
                        'S' -> point to 'a'.also { startPoint = point }
                        'E' -> point to 'z'.also { endPoint = point }
                        else -> point to char
                    }
                }
        }.toMap()

    val countMap = buildMap {
        var count = 0
        var currentCandidates = setOf(endPoint)
        while (currentCandidates.isNotEmpty()) {
            val nextCandidates = mutableSetOf<Point>()
            for (candidate in currentCandidates) {
                if (putIfAbsent(candidate, count) != null) {
                    continue
                }
                val value = parsed.getValue(candidate)
                val north = candidate.first - 1 to candidate.second
                val east = candidate.first to candidate.second + 1
                val south = candidate.first + 1 to candidate.second
                val west = candidate.first to candidate.second - 1
                for (neighbour in listOf(north, east, south, west)) {
                    parsed[neighbour]?.also {
                        if (value - it <= 1) {
                            nextCandidates += neighbour
                        }
                    }
                }
            }
            currentCandidates = nextCandidates
            count++
        }
    }

    override fun part1() = countMap[startPoint]

    override fun part2() = parsed.filter { (_, value) -> value == 'a' }.minOf { countMap[it.key] ?: Integer.MAX_VALUE }
}
