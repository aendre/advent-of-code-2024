package utils

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.client.plugins.logging.*
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import kotlin.system.measureTimeMillis
import kotlin.time.DurationUnit
import kotlin.time.toDuration

data class Solution(
    var input: String,
    val submitSolution: (part: Int, answer: Any) -> Unit,
    var part1: Any? = null,
    var part2: Any? = null
)

class AdventOfCode(private val day: Int, private val year:Int = 2024, private val block: Solution.() -> Unit) {
    private val sessionId: String = getFileContent(".session.cfg")
    private val cachedInputFile = "build/cache/input-$year-$day.txt"
    private val isInputCached = fileExists(cachedInputFile)

    suspend fun start() {
        val input = this.getPuzzleInput();
        val elapsed = measureTimeMillis {
            Solution(input, this::submitSolution, null, null).apply(block).also {
                if (it.part1!=null) println("Part 1: ${it.part1}")
                if (it.part2!=null) println("Part 2: ${it.part2}")
            }
        }
        val duration = elapsed.toDuration(DurationUnit.MILLISECONDS)
        println("-----------------------------")
        println("Execution time = $duration")
        println("Input length   = ${input.lines().count()}")
    }

    suspend fun downloadInput(): String {
        val client = HttpClient(CIO) {
            expectSuccess = true
        }
        val url = "https://adventofcode.com/$year/day/$day/input"
        val response = client.get(url) {
            cookie(name = "session", value = sessionId)
        }
        client.close()
        // Strip last new line character
        return response.bodyAsText().replace(Regex("\n$"),"")
    }

     fun submitSolution(level: Int, answer: Any) {
        val client = HttpClient(CIO) {
            expectSuccess = true
        }
        runBlocking {
            val response: HttpResponse = client.post("https://adventofcode.com/$year/day/$day/answer") {
                cookie(name = "session", value = sessionId)
                contentType(ContentType.Application.FormUrlEncoded)
                setBody("level=$level&answer=$answer")
            }

             val page = Jsoup.parse(response.bodyAsText())
             println("Answer \"$answer\" submitted for part $level:")
             println(page.select("body > main > article").text())
             println()
             client.close()
        }
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