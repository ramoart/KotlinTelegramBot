package KTB

import java.io.File

fun main() {
    val dictionary = loadDictionary()

    while (true) {
        val totalCount = dictionary.size
        val learnedWords = dictionary.filter { it.correctAnswersCount >= 3 }
        val learnedCount = learnedWords.size
        val percentLearnedWords = (learnedCount.toDouble() / totalCount * 100).toInt()

        println(
            "Меню:\n" +
                    "1 - Учить слова\n" +
                    "2 - Статистика\n" +
                    "0 - Выход"
        )

        val inputNumber = readln().toIntOrNull()
        when {
            inputNumber == 1 -> println("Выбран пункт Учить слова")
            inputNumber == 2 -> {
                println("Выбран пункт Статистика")
                println(
                    "Выучено $learnedCount из $totalCount слов | $percentLearnedWords%"
                )
                println()
            }

            inputNumber == 0 -> return
            else -> println("Введите число 1, 2 или 0")
        }
    }
}

fun loadDictionary(): MutableList<Word> {
    val wordsFile = File("words.txt")
    val dictionary = mutableListOf<Word>()

    for (line in wordsFile.readLines()) {
        val split = line.split('|')
        val correctAnswersCount = split.getOrNull(2)?.toInt() ?: 0
        val word = Word(split.getOrNull(0) ?: "", split.getOrNull(1) ?: "", correctAnswersCount)
        dictionary.add(word)
    }

    return dictionary
}
