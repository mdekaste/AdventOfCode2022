package day14

import Challenge
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

fun main() {
    Day14.part1().let(::println)
    Day14.part2().let(::println)
}

data class Point(val y: Int, val x: Int){
    fun bottomMiddle() = Point(y + 1, x)
    fun bottomLeft() = Point(y + 1, x - 1)
    fun bottomRight() = Point(y + 1, x + 1)

}
object Day14 : Challenge() {
    private val ORIGIN = Point(0, 500)
    private val depthOfLowestWall: Int
    private val grid = buildMap<Point, String> {
        input.lines().forEach { line ->
            line.split(" -> ")
                .map { point -> point.split(",").let { (x,y) -> Point(y.toInt(), x.toInt()) } }
                .zipWithNext()
                .forEach { (from, to) ->
                    for(y in min(from.y, to.y)..max(from.y, to.y)){
                        for(x in min(from.x, to.x)..max(from.x, to.x)){
                            put(Point(y,x), "WALL")
                        }
                    }
                }
        }
        depthOfLowestWall = maxOf { (e,_) -> e.y } + 2
        for(x in (ORIGIN.x - depthOfLowestWall)..(ORIGIN.x + depthOfLowestWall)){
            put(Point(depthOfLowestWall, x), "WALL")
        }
    }.toMutableMap()

    private val sandSimulation = run {
        val fallingSand = ArrayDeque(listOf(ORIGIN))
        var abyssPoint = 0
        while(grid[ORIGIN] != "SAND"){
            val grain = fallingSand.first()
            when{
                grid[grain.bottomMiddle()] == null -> fallingSand.addFirst(grain.bottomMiddle())
                grid[grain.bottomLeft()] == null -> fallingSand.addFirst(grain.bottomLeft())
                grid[grain.bottomRight()] == null -> fallingSand.addFirst(grain.bottomRight())
                else -> grid[fallingSand.removeFirst()] = "SAND"
            }
            if(grain.y == depthOfLowestWall - 2 && abyssPoint == 0){
                abyssPoint = grid.values.count { it == "SAND" }
            }
        }
        abyssPoint to grid.values.count { it == "SAND" }
    }

    override fun part1() = sandSimulation.first
    override fun part2() = sandSimulation.second
}