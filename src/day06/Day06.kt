package day06

import println
import readInput

data class Race(val duration: Long, val recordDistance: Long) {
    fun getWinningOptions(): List<Long> =
        (0..duration)
            .map { holdDuration -> (duration - holdDuration) * holdDuration }
            .filter { distance -> distance > recordDistance }
}

fun List<Race>.getCombinedRace() = Race(
    duration = joinToString(separator = "") { race -> race.duration.toString() }.toLong(),
    recordDistance = joinToString(separator = "") { race -> race.recordDistance.toString() }.toLong(),
)

fun parseRaces(input: List<String>): List<Race> {
    fun String.getNumbers() = split(" ").filter(String::isNotBlank).drop(1).map(String::toLong)

    val (durationsRow, recordsRow) = input
    val duration = durationsRow.getNumbers()
    val records = recordsRow.getNumbers()

    return List(duration.size) { index ->
        Race(duration[index], records[index])
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val races = parseRaces(input)
        return races
            .map { race -> race.getWinningOptions().size }
            .reduce { acc, numberOfOptions -> acc * numberOfOptions }
    }

    fun part2(input: List<String>): Int {
        val races = parseRaces(input)
        val combined = races.getCombinedRace()
        return combined.getWinningOptions().size
    }

    check(part1(readInput("day06/test")) == 288)
    check(part2(readInput("day06/test")) == 71503)

    val input = readInput("day06/real")
    part1(input).println()
    part2(input).println()
}
