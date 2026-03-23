package KTB

fun main(args: Array<String>) {
    val botToken = args[0]
    var updateId = 0
    var chatId: Long? = null
    val telegramBotService = TelegramBotService(botToken)

    val getUpdateId: Regex = "\"update_id\":(\\d+)".toRegex()
    val messageTextRegex: Regex = "\"text\":\"(.+?)\"".toRegex()
    val getChatId: Regex = "\"chat\":\\{\"id\":(\\d+)".toRegex()
    val callBackRegex: Regex = "\"data\":\"(.+?)\"".toRegex()

    while (true) {
        Thread.sleep(2000)
        val updates: String = telegramBotService.getUpdates(updateId)
        println(updates)

        val matchResultId: MatchResult? = getUpdateId.findAll(updates).lastOrNull()
        val groupId = matchResultId?.groups
        val id = groupId?.get(1)?.value?.toInt()
        updateId = if (id != null) id + 1 else updateId

        val matchResultText: MatchResult? = messageTextRegex.findAll(updates).lastOrNull()
        val groupText = matchResultText?.groups
        val text = groupText?.get(1)?.value ?: "нет текста"
        println(text)

        val matchResultChatId: MatchResult? = getChatId.findAll(updates).lastOrNull()
        val groupChatId = matchResultChatId?.groups
        val foundChatId = groupChatId?.get(1)?.value?.toLong()
        if (foundChatId != null) chatId = foundChatId

        val callBackData = callBackRegex.findAll(updates).lastOrNull()?.groups?.get(1)?.value

        if (text == "Hello" && chatId != null) {
            val sendMessage = telegramBotService.sendMessage(chatId, text)
            println(sendMessage)
        }

        if (text == "Menu" && chatId != null) {
            val sendMenu = telegramBotService.sendMenu(chatId)
            println(sendMenu)
        }

        if (callBackData == "learn_words_clicked" && chatId != null) {
            val sendMessage = telegramBotService.sendMessage(chatId, "Выучено 0 из 10 слов")
        }

        if (callBackData == "statistics_clicked" && chatId != null) {
            val sendMessage = telegramBotService.sendMessage(chatId, "Отсутствует статистика")
        }

    }
}

const val TELEGRAM_BASE_URL = "https://api.telegram.org/bot"