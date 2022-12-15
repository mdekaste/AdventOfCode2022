package day15

import Challenge
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.math.abs
import kotlin.time.measureTimedValue

fun main() {
    Day15.part1().let(::println)
    Day15.part2().let(::println)
}

object Day15 : Challenge() {
    val regex = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".toRegex()
    val parsed = input.lines()
        .map{ regex.matchEntire(it)!!.destructured.toList().map(String::toLong) }
        .map { (fromX, fromY, toX, toY) -> Sensor(fromY, fromX, toY, toX) }

    private fun rangeSegmentsAtHeight(height: Long) = parsed
        .asSequence()
        .mapNotNull { sensor -> sensor.sliceAtHeight(height) }
        .sortedWith(compareBy(LongRange::first).thenByDescending(LongRange::last))
        .toCollection(MultiRange())

    private const val PART_1_PARAMETER = 2_000_000L
    override fun part1() = rangeSegmentsAtHeight(PART_1_PARAMETER)
        .sumOf { it.last - it.first + 1 } - parsed.map { it.beaconY to it.beaconX }.distinct().count { it.first == PART_1_PARAMETER }

    private const val PART_2_PARAMETER = 4_000_000L
    override fun part2() = measureTimedValue {
        (0L..PART_2_PARAMETER).firstNotNullOf { height ->
            rangeSegmentsAtHeight(height)
                .takeIf { it.size == 2 }
                ?.let { ranges -> (ranges.first().last + 1) * PART_2_PARAMETER + height }
        }
    }
}

data class Sensor(
    val pointY: Long,
    val pointX: Long,
    val beaconY: Long,
    val beaconX: Long
){
    private val distanceToBeacon = abs(pointY - beaconY) + abs(pointX - beaconX)
    fun sliceAtHeight(height: Long) = distanceToBeacon
        .minus(abs(height - pointY))
        .takeIf { it > 0 }
        ?.let { (pointX - it)..(pointX + it) }
}
class MultiRange : Stack<LongRange>() {
    override fun add(element: LongRange) = when{
        isEmpty() || element.first > peek().last -> super.add(element)
        element.last < peek().last -> false
        else -> super.add(pop().first..element.last)
    }
}

