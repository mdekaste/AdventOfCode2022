object StringExtensions {
    val NON_INT_DELIMITER = """(\D)+""".toRegex()
}

fun String.splitOnEmpty() = this.split(System.lineSeparator() + System.lineSeparator())
fun String.extractInts() = this.split(StringExtensions.NON_INT_DELIMITER).mapNotNull(String::toIntOrNull)
