package day04

import kotlin.math.pow
import println
import readInput

class Card(
    private val winningNumbers: Set<Int>,
    private val gameNumbers: Set<Int>
) {
    val numberOfMatches = (winningNumbers intersect gameNumbers).size
    val value: Int get() = 2.0.pow(numberOfMatches - 1).toInt()
}

fun parseCard(cardText: String): Card {
    val (winning, game) = cardText
        .substringAfter(": ")
        .split("|")
        .map { numberSet ->
            numberSet
                .split(" ")
                .filter(String::isNotBlank)
                .map(String::toInt)
        }

    return Card(winning.toSet(), game.toSet())
}

fun main() {
    fun part1(input: List<String>): Int = input.map(::parseCard).sumOf(Card::value)

    fun part2(input: List<String>): Int {
        val counts = IntArray(input.size) { 1 } // we have at least one of each card
        input
            .map(::parseCard)
            .forEachIndexed { cardIndex, card ->
                repeat(card.numberOfMatches) { index ->
                    counts[cardIndex + index + 1] += counts[cardIndex]
                }
            }

        return counts.sum()
    }

    check(part1(readInput("day04/test")) == 13)
    check(part2(readInput("day04/test")) == 30)

    val input = readInput("day04/real")
    part1(input).println()
    part2(input).println()
}
