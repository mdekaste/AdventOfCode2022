package day15

import Challenge
import helpers.extractInts
import java.util.stream.IntStream
import kotlin.math.abs

fun main() {
    Day15.part1().let(::println)
    Day15.part2().let(::println)
}

typealias Point = Pair<Long, Long>
fun Point.distance(other: Point) = abs(first - other.first) + abs(second - other.second)

object Day15 : Challenge() {
    val regex = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".toRegex()
    val parsed = input.lines()
        .map{ regex.matchEntire(it)!!.destructured.toList().map(String::toLong) }
        .map { (fromX, fromY, toX, toY) -> (fromY to fromX) to (toY to toX) }
        .associateBy({it.first},{it.second})

    private fun rangeSegmentsAtHeight(height: Long) = buildList<LongRange> {
        parsed.entries.mapNotNull { (sensor, beacon) ->
            val distanceToBeacon = sensor.distance(beacon)
            val linePoint = height to sensor.second
            val distanceToLine = sensor.distance(linePoint)
            if(distanceToLine < distanceToBeacon){
                val widthToBridge = distanceToBeacon - distanceToLine
                (sensor.second - widthToBridge)..(sensor.second + widthToBridge)
            } else null
        }.sortedWith(compareBy<LongRange> { it.first }.thenBy { it.last }).forEach {
            when {
                isEmpty() -> add(it)
                it.first > last().last -> add(it)
                it.last < last().last -> Unit
                else -> add(removeLast().first..it.last)
            }
        }
    }

    private const val PART_1_PARAMETER = 2_000_000L
    override fun part1() = rangeSegmentsAtHeight(PART_1_PARAMETER).sumOf { it.last - it.first + 1 } -
            parsed.values.distinct().count { it.first == PART_1_PARAMETER }

    private const val PART_2_PARAMETER = 4_000_000L
    override fun part2() = (0L..PART_2_PARAMETER).firstNotNullOf { height ->
        rangeSegmentsAtHeight(height).takeIf { it.size == 2 }?.let { ranges ->
            (ranges.first().last + 1) * PART_2_PARAMETER + height
        }
    }
}