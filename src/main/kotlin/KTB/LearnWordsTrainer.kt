package KTB

import java.io.File

data class Word(
    val text: String,
    val translate: String,
    var correctAnswersCount: Int = 0,
)

data class Statistics(
    val totalCount: Int,
    val learnedCount: Int,
    val percentLearnedWords: Int,
)

data class Question(
    val variants: List<Word>,
    val correctAnswer: Word,
    val correctAnswerId: Int,
)

fun Question.asConsoleString(): String =
    buildString {
        appendLine(correctAnswer.text)
        appendLine(variants.withIndex().joinToString("\n") { (index, word) ->
            "${index + 1} - ${word.translate}"
        }
        )
    }

class LearnWordsTrainer(private val learnedAnswerCount: Int = 3, private val countOfQuestionWords: Int = 4) {
    val dictionary = loadDictionary()

    fun loadDictionary(): MutableList<Word> {
        try {
            val wordsFile = File("words.txt")
            val dictionary = mutableListOf<Word>()
            for (line in wordsFile.readLines()) {
                val split = line.split('|')
                val correctAnswersCount = split.getOrNull(2)?.toInt() ?: 0
                val word = Word(split.getOrNull(0) ?: "", split.getOrNull(1) ?: "", correctAnswersCount)
                dictionary.add(word)
            }
            return dictionary
        } catch (e: IndexOutOfBoundsException) {
            throw IllegalStateException("Некорректный файл")
        }
    }

    fun saveDictionary(dictionary: List<Word>) {
        val wordsFile = File("words.txt")
        val lines = dictionary.map { word ->
            "${word.text}|${word.translate}|${word.correctAnswersCount}"
        }
        wordsFile.writeText(lines.joinToString("\n"))
    }

    fun getStatistics(): Statistics {
        val totalCount = dictionary.size
        val learnedWords = dictionary.count {
            it.correctAnswersCount >= learnedAnswerCount
        }
        val percentLearnedWords = if (totalCount == 0) 0
        else (learnedWords.toDouble() / totalCount * 100).toInt()
        return Statistics(
            totalCount = totalCount,
            learnedCount = learnedWords,
            percentLearnedWords = percentLearnedWords
        )
    }

    fun getNextQuestion(): Question? {
        val notLearnedList = dictionary.filter { it.correctAnswersCount < learnedAnswerCount }
        if (notLearnedList.isEmpty()) return null
        val variants = if (notLearnedList.size < countOfQuestionWords) {
            val learnedList = dictionary.filter { it.correctAnswersCount >= learnedAnswerCount }.shuffled()
            notLearnedList.shuffled()
                .take(countOfQuestionWords) + learnedList.take(countOfQuestionWords - notLearnedList.size)
        } else {
            notLearnedList.shuffled().take(countOfQuestionWords)
        }.shuffled()

        val correctAnswerId = variants.indices.random()
        val correctAnswerWord = variants[correctAnswerId]

        return Question(
            variants = variants,
            correctAnswerWord,
            correctAnswerId = correctAnswerId,
        )
    }

    fun checkAnswer(question: Question, userAnswerIndex: Int?): Boolean {
        if (userAnswerIndex == null) return false

        if (userAnswerIndex == question.correctAnswerId) {
            question.correctAnswer.correctAnswersCount++
            saveDictionary(dictionary)
            return true
        }

        return false
    }
}