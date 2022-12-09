package day9

import Challenge
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.sign

fun main() {
    Day9.part1().let(::println)
    Day9.part2().let(::println)
}

data class Point(val y: Int, val x: Int)
object Day9 : Challenge() {
    val parsed = input
        .lineSequence()
        .map { it.split(" ").let { (a, b) -> a to b.toInt() } }
        .flatMap { (move, amount) -> generateSequence { move }.take(amount) }
        .map(::moveToDif)
        .runningFold(List(10) { Point(0, 0) }, ::moveRope)

    private fun moveToDif(move: String) = when (move) {
        "U" -> Point(-1, 0)
        "R" -> Point(0, 1)
        "D" -> Point(1, 0)
        "L" -> Point(0, -1)
        else -> error("unknown move")
    }

    private fun moveRope(rope: List<Point>, direction: Point): List<Point> = rope
        .drop(1)
        .runningFold(Point(rope.first().y + direction.y, rope.first().x + direction.x), ::fixTail)

    private fun fixTail(head: Point, tail: Point): Point {
        val yDif = head.y - tail.y
        val xDif = head.x - tail.x
        return when (max(yDif.absoluteValue, xDif.absoluteValue)) {
            2 -> Point(tail.y + yDif.sign, tail.x + xDif.sign)
            else -> tail
        }
    }

    override fun part1() = parsed.distinctBy { it[1] }.count()

    override fun part2() = parsed.distinctBy { it[9] }.count()
}
