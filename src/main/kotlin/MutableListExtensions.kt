object MutableListExtensions
fun <T> MutableList<T>.removeLast(amount: Int) = MutableList(amount) { removeLast() }.asReversed()
