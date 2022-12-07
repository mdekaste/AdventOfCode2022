package day7

import Challenge

fun main() {
    Day7.part1().let(::println)
    Day7.part2().let(::println)
}

object Day7 : Challenge() {
    private val root = Directory().also { root ->
        input.lineSequence()
            .map { it.split(" ") + "" } // Why no destructuring to nullable?
            .fold(root) { directory, (a, b, c) ->
                when {
                    a == "$" && b == "cd" && c == "/" -> root
                    a == "$" && b == "cd" && c == ".." -> directory.parent!!
                    a == "$" && b == "cd" -> directory.directories.getValue(c)
                    a == "$" && b == "ls" -> directory
                    a == "dir" -> directory.apply { directories[b] = Directory(directory) }
                    else -> directory.apply { updateDirectory(a.toInt()) }
                }
            }
    }
    override fun part1() = root.getAllDirectories()
        .map { it.fileSize }
        .filter { it <= 100000 }
        .sum()

    override fun part2() = root.getAllDirectories()
        .map { it.fileSize }
        .filter { it >= root.fileSize - 40000000 }
        .minOrNull()
}

class Directory(
    val parent: Directory? = null,
    val directories: MutableMap<String, Directory> = mutableMapOf(),
    var fileSize: Long = 0
) {
    fun getAllDirectories(): Sequence<Directory> = sequenceOf(this) +
        directories.values.flatMap(Directory::getAllDirectories)

    fun updateDirectory(sizeOfFile: Int) {
        fileSize += sizeOfFile
        parent?.updateDirectory(sizeOfFile)
    }
}
