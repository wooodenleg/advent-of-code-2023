package day10

import Direction.Down
import Direction.Left
import Direction.Right
import Direction.Up
import IntOffset
import getDiagonal
import plus
import println
import readInput

data class MapData(val start: Node, val pieces: Map<IntOffset, Piece>) {
    val path = buildList<Node> {
        add(start)
        add(start.validConnections(pieces).first())

        while (true) {
            val (previous, current) = takeLast(2)
            val nextStep = current.validConnections(pieces).first { node -> node != previous }

            if (nextStep == start) break
            add(nextStep)
        }
    }

    val polygon = path.map(Node::first)

    fun isPointInPolygon(point: IntOffset): Boolean {
        val rayPath = point.getDiagonal()
            .filter { rayPoint ->
                val piece = pieces[rayPoint]
                piece != Piece.DownLeft && piece != Piece.UpRight // closed corner pieces
            }.filter { rayPoint -> rayPoint in polygon }

        return (rayPath.size % 2) == 1
    }
}

enum class Piece { Ground, Horizontal, Vertical, UpRight, UpLeft, DownLeft, DownRight }
typealias Node = Pair<IntOffset, Piece>

fun Node.getConnections(): List<IntOffset> {
    val (position, piece) = this
    return when (piece) {
        Piece.Horizontal -> listOf(position + Left, position + Right)
        Piece.Vertical -> listOf(position + Up, position + Down)
        Piece.UpRight -> listOf(position + Up, position + Right)
        Piece.UpLeft -> listOf(position + Up, position + Left)
        Piece.DownLeft -> listOf(position + Down, position + Left)
        Piece.DownRight -> listOf(position + Down, position + Right)
        Piece.Ground -> emptyList()
    }
}

fun Node.validConnections(data: Map<IntOffset, Piece>): List<Node> {
    val (nodePosition, piece) = this
    return getConnections()
        .mapNotNull { position -> Pair(position, data[position] ?: return@mapNotNull null) } // neighbour nodes
        .filter { node -> nodePosition in node.getConnections() } // check if valid
}

// == PARSING
fun parsePiece(character: Char): Piece = when (character) {
    '|' -> Piece.Vertical; '-' -> Piece.Horizontal; 'L' -> Piece.UpRight; 'J' -> Piece.UpLeft
    '7' -> Piece.DownLeft; 'F' -> Piece.DownRight; '.' -> Piece.Ground
    else -> error("Unknown piece '$character'")
}

fun parseMap(input: List<String>): MapData {
    var start = IntOffset(0, 0)
    val map = buildMap {
        input.forEachIndexed { y, row ->
            row.forEachIndexed { x, piece ->
                val location = IntOffset(x, y)
                if (piece == 'S') start = location
                else set(location, parsePiece(piece))
            }
        }
    }

    val startNode = Piece.entries
        .map { piece -> start to piece }
        .first { node -> node.validConnections(map).size == 2 }

    return MapData(startNode, map + startNode)
}


fun main() {
    fun part1(input: List<String>): Int = parseMap(input).path.size / 2
    fun part2(input: List<String>): Int {
        val map = parseMap(input)
        val points = (map.pieces.keys - map.polygon.toSet())
            .filter(map::isPointInPolygon)

        return points.size
    }

    check(part1(readInput("day10/test")) == 4)
    check(part2(readInput("day10/test2")) == 4)

    val input = readInput("day10/real")
    part1(input).println()
    part2(input).println()
}
