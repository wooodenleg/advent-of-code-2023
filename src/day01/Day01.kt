package day01

import println
import readInput

val wordNumbers = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
fun getNumbers(line: String): Collection<Int> =
    sequence { for (index in line.indices) yield(line.take(index + 1)) } // get growing parts (a, ab, abc, ...)
        .mapNotNull { part ->
            val number = part.last().digitToIntOrNull()
            if (number != null) return@mapNotNull number

            val wordNumber = wordNumbers.firstOrNull(part::endsWith)
            if (wordNumber != null) wordNumbers.indexOf(wordNumber) + 1
            else null
        }
        .toList()

fun main() {
    fun part1(input: List<String>): Int = input.sumOf { line ->
        with(line.mapNotNull(Char::digitToIntOrNull)) {
            "${first()}${last()}".toInt()
        }
    }

    fun part2(input: List<String>): Int = input.sumOf { line ->
        with(getNumbers(line)) {
            "${first()}${last()}".toInt()
        }
    }

    check(part1(readInput("day01/test1")) == 142)
    check(part2(readInput("day01/test2")) == 281)

    val input = readInput("day01/real")
    part1(input).println()
    part2(input).println()
}