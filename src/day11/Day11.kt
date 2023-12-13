package day11

import IntOffset
import LongOffset
import getCombinations
import kotlin.math.abs
import println
import readInput

fun parseMap(input: List<String>): List<IntOffset> = buildList {
    input.forEachIndexed { y, row -> row.forEachIndexed { x, point -> if (point == '#') add(IntOffset(x, y)) } }
}

fun inflateSpace(map: List<IntOffset>, inflationFactor: Long = 1): List<LongOffset> {
    val columnOffsets = getOffsets(
        size = map.maxOf(IntOffset::x) + 1,
        offsetFactor = inflationFactor,
        isEmpty = { index -> map.none { offset -> offset.x == index } }
    )
    val rowOffsets = getOffsets(
        size = map.maxOf(IntOffset::y) + 1,
        offsetFactor = inflationFactor,
        isEmpty = { index -> map.none { offset -> offset.y == index } }
    )

    return map.map { offset ->
        LongOffset(
            x = offset.x + columnOffsets[offset.x],
            y = offset.y + rowOffsets[offset.y]
        )
    }
}

fun getOffsets(size: Int, offsetFactor: Long, isEmpty: (index: Int) -> Boolean) = // accumulate empty space offsets
    (0 until size).runningFold(0) { acc: Long, index: Int -> if (isEmpty(index)) acc + offsetFactor else acc }

fun main() {
    fun part1(input: List<String>): Long =
        inflateSpace(parseMap(input))
            .getCombinations()
            .sumOf { (start, end) -> abs(start.x - end.x) + abs(start.y - end.y) }

    fun part2(input: List<String>): Long =
        inflateSpace(parseMap(input), 999_999)
            .getCombinations()
            .sumOf { (start, end) -> abs(start.x - end.x) + abs(start.y - end.y) }

    check(part1(readInput("day11/test")) == 374L)

    val input = readInput("day11/real")
    part1(input).println()
    part2(input).println()
}
