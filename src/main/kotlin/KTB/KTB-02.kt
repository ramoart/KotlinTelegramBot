package KTB

import java.io.File

fun main() {
    val wordsFile: File = File("src/main/kotlin/KTB/words.txt")
    wordsFile.createNewFile()
    wordsFile.writeText("hello привет\n")
    wordsFile.appendText("dog собака\n")
    wordsFile.appendText("cat кошка")

    for (i in wordsFile.readLines()) {
        println(i)
    }
}