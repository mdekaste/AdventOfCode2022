package day14

import Challenge
import java.lang.Thread.yield
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sign

fun main() {
    Day14.part1().let(::println)
    Day14.part2().let(::println)
}

data class Point(val y: Int, val x: Int){
    fun bottomMiddle() = Point(y + 1, x)
    fun bottomLeft() = Point(y + 1, x - 1)
    fun bottomRight() = Point(y + 1, x + 1)

    fun wallTo(other: Point) = buildList {
        (y - other.y).takeIf { it != 0 }?.let {
            for(y in y..other.y step it.sign){
                add(Point(y, x))
            }
        }
        (x - other.x).takeIf { it != 0 }?.let {
            for(x in x..other.x step it.sign){
                add(Point(y, x))
            }
        }
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
                    if(from.y < to.y){
                        for(y in from.y..to.y){
                            put(Point(y,from.x), "WALL")
                        }
                    }
                    if(from.y > to.y){
                        for(y in to.y..from.y){
                            put(Point(y,from.x), "WALL")
                        }
                    }
                    if(from.x < to.x){
                        for(x in from.x..to.x){
                            put(Point(from.y,x), "WALL")
                        }
                    }
                    if(from.x > to.x){
                        for(x in to.x..from.x){
                            put(Point(from.y,x), "WALL")
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
        var fallingSand = setOf(ORIGIN)
        var settledSand = 0
        var abyssPoint = 0
        while(grid[ORIGIN] != "SAND"){
            fallingSand = buildSet {
                for(grain in fallingSand){
                    when{
                        grid[grain.bottomMiddle()] == null -> add(grain.bottomMiddle())
                        grid[grain.bottomLeft()] == null -> add(grain.bottomLeft())
                        grid[grain.bottomRight()] == null -> add(grain.bottomRight())
                        else -> {
                            grid[grain] = "SAND"
                            settledSand++
                        }
                    }
                }
                if(fallingSand.any { (y,x) -> y == depthOfLowestWall - 2 }){
                    if(abyssPoint == 0){
                        abyssPoint = settledSand
                    }
                }
                add(ORIGIN)
            }
            printGrid(grid, fallingSand)
        }
        abyssPoint to settledSand
    }

    override fun part1() = sandSimulation.first
    override fun part2() = sandSimulation.second

    private fun printGrid(map: Map<Point, String>, fallingSand: Set<Point>){
        val yMin = map.minOf { (e,_) -> e.y }
        val yMax = map.maxOf { (e,_) -> e.y }
        val xMin = map.minOf { (e,_) -> e.x }
        val xMax = map.maxOf { (e,_) -> e.x }
        for(y in yMin..yMax){
            for(x in xMin..xMax){
                print(
                    when(map[Point(y,x)]){
                        "SAND" -> "+"
                        "WALL" -> "#"
                        else -> if(fallingSand.contains(Point(y,x))){
                            '~'
                        } else {
                            '.'
                        }
                    }
                )
            }
            println()
        }
    }
}