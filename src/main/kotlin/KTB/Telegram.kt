package KTB

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun main(args: Array<String>) {
    val botToken = args[0]
    var updateId = 0
    var chatId: Long? = null
    val telegramBotService = TelegramBotService(botToken)

    while (true) {
        Thread.sleep(2000)
        val updates: String = telegramBotService.getUpdates(updateId)
        println(updates)

        val getUpdateId: Regex = "\"update_id\":(\\d+)".toRegex()
        val matchResultId: MatchResult? = getUpdateId.findAll(updates).lastOrNull()
        val groupId = matchResultId?.groups
        val id = groupId?.get(1)?.value?.toInt()
        updateId = if (id != null) id + 1 else updateId

        val messageTextRegex: Regex = "\"text\":\"(.+?)\"".toRegex()
        val matchResultText: MatchResult? = messageTextRegex.findAll(updates).lastOrNull()
        val groupText = matchResultText?.groups
        val text = groupText?.get(1)?.value ?: "нет текста"
        println(text)

        val getChatId: Regex = "\"chat\":\\{\"id\":(\\d+)".toRegex()
        val matchResultChatId: MatchResult? = getChatId.findAll(updates).lastOrNull()
        val groupChatId = matchResultChatId?.groups
        val foundChatId = groupChatId?.get(1)?.value?.toLong()
        if (foundChatId != null) chatId = foundChatId

        if (text == "Hello") {
            val sendMessage = telegramBotService.sendMessage(chatId, text)
            println(sendMessage)
        }

    }
}

const val TELEGRAM_BASE_URL = "https://api.telegram.org/bot"