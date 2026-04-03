package KTB

import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets

class TelegramBotService(private val botToken: String) {
    private val client: HttpClient = HttpClient.newBuilder().build()

    fun getUpdates(updateId: Int): String {
        val urlGetUpdates = "$TELEGRAM_BASE_URL$botToken/getUpdates?offset=$updateId"
        val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
        val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun sendMessage(chatId: Long?, sendText: String): String {
        val encodedText = URLEncoder.encode(sendText, StandardCharsets.UTF_8)
        val urlSendMessage = "$TELEGRAM_BASE_URL$botToken/sendMessage?chat_id=$chatId&text=$encodedText"
        val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlSendMessage)).build()
        val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun sendMenu(chatId: Long?): String {
        val urlSendMessage = "$TELEGRAM_BASE_URL$botToken/sendMessage"
        val sendMenuBody = """
            {
            	"chat_id": $chatId,
            	"text": "Основное меню",
            	"reply_markup": {
            		"inline_keyboard": [
            			[
            				{
            					"text": "Изучить слова",
            					"callback_data": "learn_words_clicked"
            				},
            				{
            					"text": "Статистика",
            					"callback_data": "statistics_clicked"
            				}	
            			]
            		]
            	}
            }
        """.trimIndent()
        val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlSendMessage))
            .header("Content-type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(sendMenuBody))
            .build()
        val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun sendQuestion(chatId: Long?, question: Question): String {
        val urlSendMessage = "$TELEGRAM_BASE_URL$botToken/sendMessage"
        val buttons = question.variants.mapIndexed { index, word ->
            """[{
            "text": "${word.translate}",
            "callback_data": "$CALLBACK_DATA_ANSWER_PREFIX$index"
        }]"""
        }.joinToString(",")

        val sendQuestionBody = """
        {
            "chat_id": $chatId,
            "text": "${question.correctAnswer.text}",
            "reply_markup": {
               "inline_keyboard": [
                  $buttons
               ]
            }
        }
    """.trimIndent()

        val request: HttpRequest = HttpRequest.newBuilder()
            .uri(URI.create(urlSendMessage))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(sendQuestionBody))
            .build()

        val response: HttpResponse<String> = client.send(
            request,
            HttpResponse.BodyHandlers.ofString()
        )
        return response.body()
    }

}