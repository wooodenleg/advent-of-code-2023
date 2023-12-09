package day09

import println
import readInput

fun getPrediction(numbers: List<Long>): Long {
    if (numbers.all { it == 0L }) return 0L
    val differences = numbers.zipWithNext { a, b -> b - a }
    return numbers.last() + getPrediction(differences)
}

fun parseHistory(input: List<String>) = input.map { line -> line.split(" ").map(String::toLong) }

fun main() {
    fun part1(input: List<String>): Long = parseHistory(input).sumOf(::getPrediction)
    fun part2(input: List<String>): Long = parseHistory(input).sumOf { numbers -> getPrediction(numbers.reversed()) }

    check(part1(readInput("day09/test")) == 114L)
    check(part2(readInput("day09/test")) == 2L)

    val input = readInput("day09/real")
    part1(input).println()
    part2(input).println()
}
