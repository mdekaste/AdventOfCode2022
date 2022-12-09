package helpers

import kotlin.math.absoluteValue
import kotlin.math.sign
import kotlin.math.max

data class Point(val y: Int, val x: Int) : List<Int> by listOf(y,x) {
    companion object {
        val ORIGIN = Point(0, 0)
    }
    operator fun plus(o: Point) = Point(y + o.y, x + o.x)
    operator fun minus(o: Point) = Point(y - o.y, x - o.x)
    fun sign() = Point(y.sign, x.sign)
    fun absoluteValue() = Point(y.absoluteValue, x.absoluteValue)
    fun max() = max(y, x)
}
