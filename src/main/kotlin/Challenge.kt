import java.io.File
import kotlin.system.measureTimeMillis

abstract class Challenge(
    val name: String? = null
) {
    protected val input = File(javaClass.getResource("input").path).readText()

    abstract fun part1(): Any?
    abstract fun part2(): Any?

    fun solve() = measureTimeMillis { listOf(name, part1(), part2()) }
}