package day05

import kotlin.math.min
import kotlin.time.measureTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import println
import readInput
import removeFirstWhile

class Map(
    val sourceKey: String,
    val destinationKey: String,
    private val ranges: List<Range>
) {
    fun translate(source: Long): Long {
        val range = ranges.firstOrNull { range -> source in range }
        return range?.translate(source) ?: source
    }
}

class Range(
    private val destinationStart: Long,
    private val sourceStart: Long,
    private val length: Long
) {
    private val offset = destinationStart - sourceStart
    private val sourceRange = sourceStart until (sourceStart + length)

    fun translate(source: Long): Long = source + offset
    operator fun contains(source: Long) = source in sourceRange
}

class Almanac(
    val seeds: List<Long>,
    val maps: List<Map>
) {
    private val traceCache = mutableMapOf<Pair<String, String>, List<Map>>()

    fun translate(sourceCategory: String, destinationCategory: String, value: Long): Long {
        val cacheKey = sourceCategory to destinationCategory
        val maps = if (traceCache.contains(cacheKey)) {
            traceCache[cacheKey]!!
        } else {
            buildList<Map> {
                add(maps.firstOrNull { map -> map.sourceKey == sourceCategory }!!)
                while (last().destinationKey != destinationCategory) {
                    add(maps.firstOrNull { map -> map.sourceKey == last().destinationKey }!!)
                }
            }.also { traceCache[cacheKey] = it }
        }

        return maps.fold(value) { acc, map -> map.translate(acc) }
    }

    fun getSeedLocations(): List<Long> = seeds.map { seed -> translate("seed", "location", seed) }
    suspend fun getMinSeedRangeLocation(): Long {
        val seedRanges = seeds.chunked(2)

        return coroutineScope {
            val deferredRanges = seedRanges.map { (start, length) ->
                async {
                    var min = Long.MAX_VALUE
                    for (seed in start until (start + length))
                        min = min(translate("seed", "location", seed), min)
                    min
                }
            }

            deferredRanges.awaitAll().min()
        }
    }
}

fun parseAlmanac(input: List<String>): Almanac {
    val inputDequeue = ArrayDeque(input + "") // add empty lune at the end

    val seeds = inputDequeue.removeFirst().substringAfter("seeds: ").split(" ").map(String::toLong)
    inputDequeue.removeFirst()

    val maps = mutableListOf<Map>()
    while (inputDequeue.isNotEmpty()) {
        val (source, _, target) = inputDequeue.removeFirst().substringBefore(" ").split("-")
        val ranges = inputDequeue
            .removeFirstWhile(String::isNotBlank)
            .map { rangeLine ->
                val (destination, source, length) = rangeLine.split(" ").map(String::toLong)
                Range(destination, source, length)
            }

        maps.add(Map(source, target, ranges))
        inputDequeue.removeFirst() // remove empty line
    }

    return Almanac(seeds, maps)
}

fun main() {
    fun part1(input: List<String>): Long {
        val almanac = parseAlmanac(input)
        return almanac.getSeedLocations().min()
    }

    fun part2(input: List<String>): Long {
        val almanac = parseAlmanac(input)
        return runBlocking(Dispatchers.Default) {
            almanac.getMinSeedRangeLocation()
        }
    }

    check(part1(readInput("day05/test")) == 35L)
    check(part2(readInput("day05/test")) == 46L)

    val input = readInput("day05/real")
    part1(input).println()

    val time = measureTime {
        part2(input).println()
    }
    println("${time.inWholeSeconds} seconds") // 56 :(
}
