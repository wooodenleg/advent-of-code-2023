import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.hypot

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun <T> ArrayDeque<T>.removeFirstWhile(condition: (T) -> Boolean): List<T> {
    val removedElements = mutableListOf<T>()
    while (isNotEmpty() && condition(first())) {
        removedElements.add(removeFirst())
    }
    return removedElements
}

fun gcd(a: Long, b: Long): Long = if (a == 0L) b else gcd(b % a, a)
fun lcm(a: Long, b: Long): Long = a * b / gcd(a, b)

fun lcm(numbers: List<Int>): Long {
    var result = 1L

    for (i in numbers.indices) {
        result = lcm(numbers[i].toLong(), result)
        if (result == 0L) return result
    }

    return result
}

enum class Direction(val offset: IntOffset) {
    Up(IntOffset(0, -1)),
    Right(IntOffset(1, 0)),
    Down(IntOffset(0, 1)),
    Left(IntOffset(-1, 0)),
}

data class IntOffset(val x: Int, val y: Int)

operator fun IntOffset.plus(offset: IntOffset) = IntOffset(x + offset.x, y + offset.y)
operator fun IntOffset.minus(offset: IntOffset) = IntOffset(x - offset.x, y - offset.y)
fun IntOffset.distanceTo(offset: IntOffset): Int = hypot(offset.x - x.toFloat(), offset.y - y.toFloat()).toInt()
operator fun IntOffset.plus(direction: Direction) = this + direction.offset
fun IntOffset.getDiagonal(): List<IntOffset> = sequence {
    var current = this@getDiagonal
    while (current.x >= 0 && current.y >= 0) {
        yield(current)
        current -= IntOffset(1, 1)
    }
}.toList()

fun List<Direction>.calculatePosition() = fold(IntOffset(0, 0)) { acc, dir -> acc + dir.offset }