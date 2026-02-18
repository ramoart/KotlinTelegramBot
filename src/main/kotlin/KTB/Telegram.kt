package KTB

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun main(args: Array<String>) {
    val botToken = args[0]
    var updateId = 0
    var chatId: Int? = null

    while (true) {
        Thread.sleep(2000)
        val updates: String = getUpdates(botToken, updateId)
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
        val foundChatId = groupChatId?.get(1)?.value?.toInt()
        if (foundChatId != null) chatId = foundChatId

        println("Напишите сообщение для отправки боту")
        val sendText = readln()
        val sendMessage = sendMessage(botToken, chatId, sendText)
        println(sendMessage)
    }
}

fun getUpdates(botToken: String, updateId: Int): String {
    val urlGetUpdates = "$TELEGRAM_BASE_URL$botToken/getUpdates?offset=$updateId"
    val client: HttpClient = HttpClient.newBuilder().build()
    val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
    val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
}

fun sendMessage(botToken: String, chatId: Int?, sendText: String): String {
    val urlSendMessage = "$TELEGRAM_BASE_URL$botToken/sendMessage?chat_id=$chatId&text=$sendText"
    val client: HttpClient = HttpClient.newBuilder().build()
    val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlSendMessage)).build()
    val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
}

const val TELEGRAM_BASE_URL = "https://api.telegram.org/bot"