package day00

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    check(part1(readInput("day00/test1")) == 0)
    check(part2(readInput("day00/test2")) == 0)

    val input = readInput("day00/real")
    part1(input).println()
    part2(input).println()
}
