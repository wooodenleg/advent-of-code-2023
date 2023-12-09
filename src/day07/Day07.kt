package day07

import day07.HandType.*
import println
import readInput

val cardValues = listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')
val jokerCardValues = listOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')

enum class HandType { HighCard, OnePair, TwoPairs, ThreeOfKind, FullHouse, FourOfKind, FiveOfKind }

fun getHandType(hand: List<Char>): HandType {
    val values = hand.groupBy { it }
        .values.toList()
        .sortedByDescending(List<Char>::size)

    if (values.size == 1) return FiveOfKind

    val (typeA, typeB) = values

    if (typeA.size == 4 && typeB.size == 1) return FourOfKind
    if (typeA.size == 3 && typeB.size == 2) return FullHouse

    val typeC = values[2]
    if (typeA.size == 3 && typeB.size == 1 && typeC.size == 1) return ThreeOfKind
    if (typeA.size == 2 && typeB.size == 2 && typeC.size == 1) return TwoPairs
    if (values.size == 4) return OnePair

    return HighCard
}

class Game(val hand: List<Char>, val score: Long, val jokersEnabled: Boolean) : Comparable<Game> {
    private val handType: HandType = if (jokersEnabled) {
        cardValues.map { card ->
            hand.map { handCard -> if (handCard == 'J') card else handCard }
        }.maxOf(::getHandType)
    } else {
        getHandType(hand)
    }

    override operator fun compareTo(other: Game): Int =
        when {
            handType.ordinal > other.handType.ordinal -> 1
            handType.ordinal < other.handType.ordinal -> -1
            else -> { // equals
                val thisValues = if (jokersEnabled) {
                    hand.map(jokerCardValues::indexOf)
                } else hand.map(cardValues::indexOf)

                val otherValues = if (jokersEnabled) {
                    other.hand.map(jokerCardValues::indexOf)
                } else other.hand.map(cardValues::indexOf)

                repeat(5) { index ->
                    if (thisValues[index] > otherValues[index]) return 1
                    else if (thisValues[index] < otherValues[index]) return -1
                }

                0
            }
        }
}

fun parseGames(input: List<String>, jokersEnabled: Boolean): List<Game> = input.map { line ->
    val (hand, score) = line.split(" ")
    Game(hand.toList(), score.toLong(), jokersEnabled)
}

fun main() {
    fun part1(input: List<String>): Long {
        val games = parseGames(input, false)
            .sorted()
            .mapIndexed { index, game -> game.score * (index + 1) }

        return games.sum()
    }

    fun part2(input: List<String>): Long {
        val games = parseGames(input, true)
            .sorted()
            .mapIndexed { index, game -> game.score * (index + 1) }

        return games.sum()
    }

    check(part1(readInput("day07/test")) == 6440L)
    check(part2(readInput("day07/test")) == 5905L)

    val input = readInput("day07/real")
    part1(input).println()
    part2(input).println()
}
