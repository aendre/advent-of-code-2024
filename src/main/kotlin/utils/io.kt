package utils
import java.io.File

fun getFileContent(filename: String): String {
    return File(filename).readText(Charsets.UTF_8)
}

fun writeContent(filename: String, fileContent: String) {
    File(filename).writeText(fileContent)
}

fun appendContent(filename: String, fileContent: String) {
    File(filename).appendText(fileContent)
}

fun fileExists(filename: String) = File(filename).isFile

fun gray(str:String) = "\u001b[90m" + str + "\u001b[0m"