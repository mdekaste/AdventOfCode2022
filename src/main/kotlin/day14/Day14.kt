package day14

import Challenge
import kotlin.math.sign

fun main() {
    Day14.part1().let(::println)
    Day14.part2().let(::println)
}

data class Point(val y: Int, val x: Int){
    fun bottomMiddle() = Point(y + 1, x)
    fun bottomLeft() = Point(y + 1, x - 1)
    fun bottomRight() = Point(y + 1, x + 1)

    fun wallTo(to: Point) = buildList {
        val pointDif = Point((to.y - y).sign, (to.x - x).sign)
        generateSequence(this@Point){ a -> Point(a.y + pointDif.y, a.x + pointDif.x) }
            .takeWhile { it != to }
            .forEach { add(Point(it.y, it.x)) }
        add(to)
    }

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
                    from.wallTo(to).forEach { put(Point(it.y, it.x), "WALL") }
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