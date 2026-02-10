package KTB

fun main() {
    val trainer = LearnWordsTrainer()

    while (true) {
        println(
            "Меню:\n" +
                    "1 - Учить слова\n" +
                    "2 - Статистика\n" +
                    "0 - Выход"
        )

        val inputNumber = readln().toIntOrNull()
        when (inputNumber) {
            1 -> {
                println("Выбран пункт Учить слова")
                while (true) {
                    val question = trainer.getNextQuestion()
                    if (question == null) {
                        println("Все слова в словаре выучены")
                        println()
                        break
                    }
                    println()
                    println("Выберете правильный ответ или выйдете в Меню:")
                    println(question.asConsoleString())
                    println("----------")
                    println("0 - Меню")

                    val userAnswerInput = readln().toIntOrNull()
                    val userAnswerIndex = userAnswerInput?.minus(1)
                    when {
                        userAnswerInput == 0 -> break
                        trainer.checkAnswer(question, userAnswerIndex) -> {
                            println("Правильно!")
                        }

                        else -> {
                            println(
                                "Неправильно! ${question.correctAnswer.text} - " +
                                        "это ${question.correctAnswer.translate}"
                            )
                        }
                    }
                }
            }

            2 -> {
                println("Выбран пункт Статистика")
                val stats = trainer.getStatistics()
                if (stats.totalCount == 0) {
                    println("Словарь не был загружен, статистика отсутствует")
                } else {
                    println(
                        "Выучено ${stats.learnedCount} из ${stats.totalCount} слов | ${stats.percentLearnedWords}%"
                    )
                }
            }

            0 -> return
            else -> println("Введите число 1, 2 или 0")
        }
    }
}

const val CORRECT_COUNT_CHECK = 3