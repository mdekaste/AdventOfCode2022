package day17

import Challenge

fun main() {
    Day17.part1().let(::println)
    Day17.part2().let(::println)
}

typealias Point = Pair<Int, Int>
typealias ShapeSpace = List<Point>
fun ShapeSpace.moveRight(set: Set<Point>): ShapeSpace {
    return this.map { it.first to it.second + 1 }
        .takeIf { it.none(set::contains) && it.none{ it.second > 6 } }
        ?: this
}
fun ShapeSpace.moveLeft(set: Set<Point>) = map { it.first to it.second - 1 }
    .takeIf { it.none(set::contains) && it.none{ it.second < 0 } }
    ?: this@moveLeft
fun ShapeSpace.moveDown(set: MutableSet<Point>) = map { it.first - 1 to it.second }
    .takeIf { it.none(set::contains) && it.none{ it.first < 0 }}
    ?: run {
        set += this
        null
    }
object Day17 : Challenge() {
    override fun part1(): Any? {
        var ordinal = 0
        var shape: ShapeSpace? = null
        val set = mutableSetOf<Point>()
        ordinal@while(ordinal <= 2022) {
            for (move in input) {
                if (shape == null) {
                    val height = set.maxOfOrNull { it.first }?.plus(1) ?: 0
                    shape = Shape.values()[ordinal.mod(5)].offsets.map { it.first + height + 3 to it.second + 2 }
                    ordinal += 1
                    if(ordinal > 2022){
                        break@ordinal
                    }
                }
                //prettyPrint(set, shape)
                shape = if (move == '>') {
                    shape.moveRight(set)
                } else {
                    shape.moveLeft(set)
                }
                shape = shape.moveDown(set)
            }
        }
        return 1 + (set.maxOfOrNull { it.first } ?: 0)
    }

    override fun part2(): Any? {
        return null
    }

    fun prettyPrint(set: Set<Point>, shape: ShapeSpace? = null){
        val maxY = (set.maxOfOrNull { it.first } ?: 0) + 6
        for(y in maxY downTo 0){
            print("|")
            for(x in 0..6){
                if(set.contains(y to x)){
                    print("#")
                } else if(y to x in shape ?: emptyList()){
                    print ("@")
                } else {
                    print(".")
                }
            }
            print("|")
            println()
        }
        println("------------------------")
    }
}

enum class Shape(val offsets: List<Point>){
    MINUS(
        listOf(
            0 to 0,
            0 to 1,
            0 to 2,
            0 to 3
        )
    ),
    PLUS(
        listOf(
            0 to 1,
            1 to 0,
            1 to 1,
            1 to 2,
            2 to 1
        )
    ),
    REVERSE_L(
        listOf(
            0 to 0,
            0 to 1,
            0 to 2,
            1 to 2,
            2 to 2
        )
    ),
    STANDING(
        listOf(
            0 to 0,
            1 to 0,
            2 to 0,
            3 to 0
        )
    ),
    CUBE(
        listOf(
            0 to 0,
            0 to 1,
            1 to 0,
            1 to 1
        )
    );
    val width = offsets.maxOf { it.second } + 1
}