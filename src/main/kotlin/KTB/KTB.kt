package KTB

import java.io.File

fun main() {
    val dictionary = loadDictionary()

    while (true) {
        println(
            "Меню:\n" +
                    "1 - Учить слова\n" +
                    "2 - Статистика\n" +
                    "0 - Выход"
        )

        val inputNumber = readln().toIntOrNull()
        when (inputNumber) {
            1 ->
                while (true) {
                    println("Выбран пункт Учить слова")
                    val notLearnedList = dictionary.filter { it.correctAnswersCount < CORRECT_COUNT_CHECK }
                    if (notLearnedList.isEmpty()) {
                        println("Все слова в словаре выучены")
                        println()
                        break
                    }
                    val questionWords = notLearnedList.shuffled().take(4)
                    val correctAnswer = questionWords.random()
                    println()
                    println("${correctAnswer.text}:")
                    questionWords.shuffled().forEachIndexed { index, word ->
                        println("${index + 1} - ${word.translate}")
                    }
                    println("Выберете правильный ответ")
                    val userAnswerInput = readln()
                }

            2 -> {
                println("Выбран пункт Статистика")
                val totalCount = dictionary.size
                if (totalCount == 0) println("Словарь не был загружен, статистика отсутствует")
                else {
                    val learnedWords = dictionary.filter { it.correctAnswersCount >= CORRECT_COUNT_CHECK }
                    val learnedCount = learnedWords.size
                    val percentLearnedWords = (learnedCount.toDouble() / totalCount * 100).toInt()
                    println(
                        "Выучено $learnedCount из $totalCount слов | $percentLearnedWords%"
                    )
                }
                println()
            }

            0 -> return
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

const val CORRECT_COUNT_CHECK = 3