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
                    val correctAnswerId = questionWords.indices.random()
                    val correctAnswerWord = questionWords[correctAnswerId]
                    println()
                    println("Выберете правильный ответ или выйдете в Меню:")
                    println(correctAnswerWord.text)
                    questionWords.forEachIndexed { index, word ->
                        println("${index + 1} - ${word.translate}")
                    }
                    println("----------")
                    println("0 - Меню")
                    val userAnswerInput = readln().toInt()
                    val userAnswerIndex = userAnswerInput - 1
                    when {
                        userAnswerInput == 0 -> break
                        userAnswerIndex == correctAnswerId -> {
                            println("Правильно!")
                            correctAnswerWord.correctAnswersCount++
                            saveDictionary(dictionary)
                        }

                        userAnswerIndex != correctAnswerId -> println(
                            "Неправильно! ${correctAnswerWord.text} - " +
                                    "это ${correctAnswerWord.translate}"
                        )
                    }
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

fun saveDictionary(dictionary: List<Word>) {
    val wordsFile = File("words.txt")
    val lines = dictionary.map { word ->
        "${word.text}|${word.translate}|${word.correctAnswersCount}"
    }
    wordsFile.writeText(lines.joinToString("\n"))
}

const val CORRECT_COUNT_CHECK = 3