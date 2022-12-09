package day9

import Challenge
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.sign

fun main() {
    Day9.part1().let(::println)
    Day9.part2().let(::println)
}

typealias Point = Pair<Int, Int>
object Day9 : Challenge() {
    val parsed = input
        .lineSequence()
        .map { it.split(" ").let { (a, b) -> a to b.toInt() } }
        .flatMap { (move, amount) -> generateSequence { move }.take(amount) }
        .map(::moveToDif)
        .runningFold(List(10) { 0 to 0 }, ::moveRope)

    private fun moveToDif(move: String) = when (move) {
        "U" -> -1 to 0
        "R" -> 0 to 1
        "D" -> 1 to 0
        "L" -> 0 to -1
        else -> error("unknown move")
    }

    private fun moveRope(rope: List<Point>, direction: Point): List<Point> = rope
        .drop(1)
        .runningFold(rope.first().first + direction.first to rope.first().second + direction.second, ::fixTail)

    private fun fixTail(head: Point, tail: Point): Point {
        val yDif = head.first - tail.first
        val xDif = head.second - tail.second
        return when (max(yDif.absoluteValue, xDif.absoluteValue)) {
            2 -> tail.first + yDif.sign to tail.second + xDif.sign
            else -> tail
        }
    }

    override fun part1() = parsed.distinctBy { it[1] }.count()

    override fun part2() = parsed.distinctBy { it[9] }.count()
}
