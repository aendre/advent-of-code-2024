package utils

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlin.system.measureTimeMillis
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class AdventOfCode(private val day: Int, private val year:Int = 2024, private val solution: (String) -> Unit) {

    private val client = HttpClient(CIO) {
        expectSuccess = true
    }
    private val sessionId: String = getFileContent(".session.cfg")
    private val cachedInputFile = "build/cache/input-$year-$day.txt"
    private val isInputCached = fileExists(cachedInputFile)

    suspend fun start() {
        val input = this.getPuzzleInput();
        val elapsed = measureTimeMillis {
            solution(input)
        }
        val duration = elapsed.toDuration(DurationUnit.MILLISECONDS)
        println("-----------------------------")
        println("Execution time = $duration")
        println("Input length   = ${input.lines().count()}")
    }

    suspend fun downloadInput(): String {
        val url = "https://adventofcode.com/$year/day/$day/input"
        val response = client.get(url) {
            cookie(name = "session", value = sessionId)
        }
        client.close()
        // Strip last new line character
        return response.bodyAsText().replace(Regex("\n$"),"")
    }

    suspend fun getPuzzleInput(): String {
        val input: String
        if (isInputCached) {
            println("Reading input from cache for Day $day, $year")
            input = getFileContent(cachedInputFile)
        }
        else {
            println("Downloading input for Day $day, $year")
            input = downloadInput()
            writeContent(cachedInputFile, input)
        }
        println("----------------------------------------")
        return input
    }
}