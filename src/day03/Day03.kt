package day03

import println
import readInput

class Schematic(val numbers: List<Number>, val symbols: List<CharCoord>) {
    val validNumbers: List<Number>
        get() = numbers.filter { number ->
            val potentialSymbolCoords = number.potentialSymbolCoords
            symbols
                .map(CharCoord::coordinates)
                .any(potentialSymbolCoords::contains)
        }

    val totalGearRatio
        get() = symbols
            .filter { symbol -> symbol.char == '*' }
            .mapNotNull { gearSymbol ->
                val adjacentNumbers = numbers.filter { number ->
                    gearSymbol.coordinates in number.potentialSymbolCoords
                }

                if (adjacentNumbers.size == 2) {
                    val (first, second) = adjacentNumbers
                    first.value * second.value
                } else null
            }
            .sum()
}

data class CharCoord(val char: Char, val coordinates: Pair<Int, Int>)

data class Number(val digits: List<CharCoord>) {
    val value by lazy { digits.map(CharCoord::char).toCharArray().concatToString().toInt() }
    val potentialSymbolCoords: List<Pair<Int, Int>> by lazy {
        val lineIndex = digits.first().coordinates.first
        val firstIndex = digits.first().coordinates.second
        val lastIndex = digits.last().coordinates.second
        ((firstIndex - 1)..(lastIndex + 1)) // X coord range
            .flatMap { index -> listOf(Pair(lineIndex - 1, index), Pair(lineIndex, index), Pair(lineIndex + 1, index)) }
    }

    operator fun plus(coordinates: CharCoord) = Number(digits + coordinates)
}

fun parseSchematic(lines: List<String>): Schematic {
    val numbers = lines.flatMapIndexed { lineIndex, line ->
        if (line.none(Char::isDigit)) return@flatMapIndexed emptyList() // ignore lines with no numbers
        val digitCoords = line.mapIndexedNotNull { index, char ->
            if (char.isDigit()) CharCoord(char, Pair(lineIndex, index))
            else null
        }

        val numbers = mutableListOf<Number>()
        var tempNumber = Number(listOf(digitCoords.first()))
        for (digit in digitCoords.drop(1)) {
            val lastDigitIndex = tempNumber.digits.last().coordinates.second
            val currentDigitIndex = digit.coordinates.second

            if (lastDigitIndex == currentDigitIndex - 1) tempNumber += digit
            else {
                numbers.add(tempNumber)
                tempNumber = Number(listOf(digit))
            }
        }
        numbers += tempNumber // Add number in buffer
        numbers
    }

    val symbols = lines.flatMapIndexed { lineIndex, line ->
        line.mapIndexedNotNull { index, character ->
            if (character.isDigit() || character == '.') null // ignore digits and dots
            else CharCoord(character, lineIndex to index)
        }
    }

    return Schematic(numbers, symbols)
}

fun main() {
    fun part1(schematic: Schematic): Int = schematic.validNumbers.sumOf(Number::value)
    fun part2(schematic: Schematic): Int = schematic.totalGearRatio

    val testSchematic = parseSchematic(readInput("day03/test"))
    check(part1(testSchematic) == 4361)
    check(part2(testSchematic) == 467835)

    val realSchematic = parseSchematic(readInput("day03/real"))
    part1(realSchematic).println()
    part2(realSchematic).println()
}