package KTB

import java.io.File

fun main() {
    val wordsFile = File("words.txt")
    val dictionary = mutableListOf<Word>()

    for (line in wordsFile.readLines()) {
        val split= line.split('|')
        val correctAnswersCount = split.getOrNull(2)?.toInt() ?: 0
        val word = Word(split[0], split[1], correctAnswersCount)
        dictionary.add(word)
    }

    println(dictionary)
}