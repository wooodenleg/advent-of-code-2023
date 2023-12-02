package day02

import kotlin.math.max
import println
import readInput

class Game(val id: Int, val sets: List<CubeSet>)
data class CubeSet(val redCount: Int, val greenCount: Int, val blueCount: Int)

fun parseCubeSet(text: String): CubeSet = text
    .split(", ")
    .map(String::trim)
    .associate { cubeCount ->
        val (count, name) = cubeCount.split(" ")
        name to count.toInt()
    }.let { cubes ->
        CubeSet(cubes["red"] ?: 0, cubes["green"] ?: 0, cubes["blue"] ?: 0)
    }

val Game.smallestCombinedSet
    get() = sets.reduce { (maxRed, maxGreen, maxBlue), (currentRed, currentGreen, currentBlue) ->
        CubeSet(
            redCount = max(maxRed, currentRed),
            greenCount = max(maxGreen, currentGreen),
            blueCount = max(maxBlue, currentBlue)
        )
    }

val CubeSet.power: Int get() = redCount * greenCount * blueCount

fun main() {
    fun parseGames(input: List<String>) = input.map { line ->
        val (game, sets) = line.split(":")
        val gameId = game.split(" ").last().toInt()
        val cubeSets = sets.split(";").map(::parseCubeSet)

        Game(gameId, cubeSets)
    }

    fun part1(input: List<String>): Int =
        parseGames(input)
            .filter { game -> game.sets.all { it.redCount <= 12 && it.greenCount <= 13 && it.blueCount <= 14 } }
            .sumOf(Game::id)

    fun part2(input: List<String>): Int =
        parseGames(input)
            .map(Game::smallestCombinedSet)
            .sumOf(CubeSet::power)

    check(part1(readInput("day02/test")) == 8)
    check(part2(readInput("day02/test")) == 2286)

    val input = readInput("day02/real")
    part1(input).println()
    part2(input).println()
}
