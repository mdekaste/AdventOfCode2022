package day13

import Challenge


fun main() {
    Day13.part1().let(::println)
    Day13.part2().let(::println)
}

object Day13 : Challenge() {
    private val regex2 = """(\d+|]|\[)""".toRegex()
    val parsed = input.lines().filter(String::isNotBlank).map(::lineToNode)

    private fun lineToNode(line: String) = buildList<MutableList<Node>> {
        add(mutableListOf())
        regex2.findAll(line).map(MatchResult::value).forEach { cs ->
            when(cs){
                "[" -> add(mutableListOf())
                "]" -> removeLast().also { last().add(Holder(*it.toTypedArray())) }
                else -> last().add(Value(cs.toInt()))
            }
        }
    }[0][0]


    override fun part1() = parsed.chunked(2)
        .mapIndexed { index, (a,b) -> if(a < b) index + 1 else 0 }
        .sum()

    override fun part2(): Any? {
        val packet1 = Holder(Holder(Value(2)))
        val packet2 = Holder(Holder(Value(6)))
        val list = (parsed + packet1 + packet2).sorted()
        return (1 + list.indexOf(packet1)) * (1 + list.indexOf(packet2))
    }
}

sealed interface Node : Comparable<Node>

class Holder(vararg val nodes: Node) : Node{
    override fun compareTo(other: Node): Int = when(other){
        is Value -> compareTo(Holder(other))
        is Holder -> nodes.zip(other.nodes, Node::compareTo).firstOrNull { it != 0 }
            ?: nodes.size.compareTo(other.nodes.size)
    }
}
class Value(val int: Int) : Node {
    override fun compareTo(other: Node): Int = when(other){
        is Value -> int.compareTo(other.int)
        is Holder -> Holder(this).compareTo(other)
    }
}


