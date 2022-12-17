package day16

import Challenge
import java.util.PriorityQueue
import kotlin.math.max

fun main() {
    Day16.part1().let(::println)
    Day16.part2().let(::println)
}


typealias ValveData = Triple<String, Int, List<String>>
object Day16 : Challenge() {
    val regex1 = """Valve (.+) has flow rate=(\d+)""".toRegex()
    val parsed = input.lines().associate {
        it.split(";").let { (a, b) ->
            val valve = a.substringAfter("Valve ").substringBefore(" has")
            val flow = a.substringAfter("=").toInt()
            val valves: List<String> = when(b.contains("valve ")){
                true -> listOf(b.substringAfter("valve "))
                false -> b.substringAfterLast("valves ").split(", ")
            }
            valve to  (flow to valves)
        }
    }

    override fun part1(): Any? {
        return visited("AA", mutableSetOf(), 0, 30)
    }

    fun visited(name: String, openedValved: MutableSet<String>, releasedPressure: Long, minutesLeft: Int): Long{
        val (flow, valves) = parsed.getValue(name)
        return when  {
            minutesLeft == 0 || openedValved.size == parsed.keys.size -> releasedPressure
            else -> {
                var candidate: Long = 0
                if(flow != 0 && name !in openedValved){
                    openedValved.add(name)
                    candidate = visited(name, openedValved, releasedPressure + (minutesLeft - 1) * flow, minutesLeft - 1)
                    openedValved.remove(name)
                }
                max(candidate, valves.maxOf { visited(it, openedValved, releasedPressure, minutesLeft - 1) })
            }
        }
    }

    override fun part2(): Any? {
        return null
    }
}