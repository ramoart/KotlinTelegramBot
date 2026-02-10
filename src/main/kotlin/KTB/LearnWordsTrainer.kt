package KTB

import java.io.File

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

class LearnWordsTrainer {
    val dictionary = loadDictionary()

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

    fun getStatistics(): Statistics {
        val totalCount = dictionary.size
        val learnedWords = dictionary.count {
            it.correctAnswersCount >= CORRECT_COUNT_CHECK
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
        val notLearnedList = dictionary.filter { it.correctAnswersCount < CORRECT_COUNT_CHECK }
        if (notLearnedList.isEmpty()) return null
        val variants = notLearnedList.shuffled().take(4)
        val correctAnswerId = variants.indices.random()
        val correctAnswerWord = variants[correctAnswerId]

        return Question(
            variants = variants,
            correctAnswerWord,
            correctAnswerId = correctAnswerId,
        )
    }


}


