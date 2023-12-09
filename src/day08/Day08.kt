package day08

import lcm
import println
import readInput

data class Plan(
    val directions: List<Direction>,
    val branches: Map<String, Branch>
) {
    val preCalculatedDestinations: Map<String, String> =
        branches.keys.associateWith { start ->
            directions.fold(start) { position, direction -> branches[position]!!.getNextBranch(direction) }
        }

    val preCalculatedStepsToEnd: Map<String, Int> =
        branches.keys
            .filter { it.endsWith("A") }
            .associateWith { start ->
                var position = start;
                var steps = 0
                while (!position.endsWith("Z")) {
                    position = preCalculatedDestinations[position]!!
                    steps += directions.size
                }
                steps
            }

}

enum class Direction { Left, Right }
data class Branch(val left: String, val right: String) {
    fun getNextBranch(direction: Direction) = when (direction) {
        Direction.Left -> left
        Direction.Right -> right
    }
}

fun parsePlan(input: List<String>): Plan {
    val directions = input.first().map { direction ->
        if (direction == 'R') Direction.Right
        else Direction.Left
    }

    val branches = input.drop(2).associate { line ->
        val (name, _, left, right) = line.replace(Regex("[(,)]"), "").split(" ")
        name to Branch(left, right)
    }

    return Plan(directions, branches)
}

fun main() {
    fun part1(input: List<String>): Int = parsePlan(input).preCalculatedStepsToEnd["AAA"]!!

    fun part2(input: List<String>): Long {
        val steps = parsePlan(input).preCalculatedStepsToEnd
        return lcm(steps.values.toList())
    }

    check(part1(readInput("day08/test")) == 6)
    check(part2(readInput("day08/test")) == 6L)

    val input = readInput("day08/real")
    part1(input).println()
    part2(input).println()
}
