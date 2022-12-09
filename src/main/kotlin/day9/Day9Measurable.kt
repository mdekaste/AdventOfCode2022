package day9

import Challenge
import helpers.Point
import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main() {
    repeat(1000) {
        val dag = Day9Measurable()
        dag.part1()
        dag.part2()
    }
    measureTime {
        Day9Measurable().run {
            part1()
            part2()
        }
    }.let(::println)
}
class Day9Measurable : Challenge() {

    companion object {
        val inputFile = File(javaClass.getResource("input").path).readText()
    }

    val parsed = inputFile
        .lineSequence()
        .map { it.split(" ").let { (a, b) -> a to b.toInt() } }
        .flatMap { (move, amount) -> generateSequence { move }.take(amount) }
        .map(::moveToDif)
        .runningFold(List(10) { Point.ORIGIN }, ::moveRope)

    private fun moveToDif(move: String) = when (move) {
        "U" -> Point(-1, 0)
        "R" -> Point(0, 1)
        "D" -> Point(1, 0)
        "L" -> Point(0, -1)
        else -> error("unknown move")
    }

    private fun moveRope(rope: List<Point>, direction: Point): List<Point> = rope
        .drop(1)
        .runningFold(rope.first() + direction, ::fixTail)

    private fun fixTail(head: Point, tail: Point): Point {
        val pointDif = head - tail
        return when (pointDif.absoluteValue().max()) {
            2 -> tail + pointDif.sign()
            else -> tail
        }
    }

    override fun part1() = parsed.distinctBy { it[1] }.count()

    override fun part2() = parsed.distinctBy { it[9] }.count()
}
