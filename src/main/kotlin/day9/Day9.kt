package day9

import Challenge
import helpers.Point

fun main() {
    Day9.part1().let(::println)
    Day9.part2().let(::println)
}
object Day9 : Challenge() {
    val parsed = input
        .lines()
        .map { it.split(" ").let { (a, b) -> a to b.toInt() } }
        .flatMap { (move, amount) -> generateSequence { move }.take(amount) }
        .map(::moveToDif)

    private fun moveToDif(move: String) = when (move) {
        "U" -> Point(-1, 0)
        "R" -> Point(0, 1)
        "D" -> Point(1, 0)
        "L" -> Point(0, -1)
        else -> error("unknown move")
    }

    private fun solve(ropeSize: Int) = parsed
        .runningFold(List(ropeSize) { Point.ORIGIN }, ::moveRope)
        .distinctBy(List<*>::last)
        .size

    private fun moveRope(rope: List<Point>, direction: Point): List<Point> = rope
        .drop(1)
        .runningFold(rope.first() + direction, ::fixTail)

    private fun fixTail(head: Point, tail: Point): Point {
        val pointDif = head - tail
        val pointStep = pointDif.sign()
        return if(pointDif == pointStep) tail else tail + pointStep
    }

    override fun part1() = solve(2)

    override fun part2() = solve(10)
}
