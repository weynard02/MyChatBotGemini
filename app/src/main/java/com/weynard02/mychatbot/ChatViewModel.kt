package com.weynard02.mychatbot

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import com.weynard02.mychatbot.ui.data.MessageModal
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    val messageList by lazy {
        mutableStateListOf<MessageModal>()
    }
    val generativeModel = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel("gemini-2.5-flash")

    fun sendMessage(question: String) {
        viewModelScope.launch {
            try {
                val chat = generativeModel.startChat(
                    history = messageList.map {
                        content(it.role) { text(it.message) }
                    }.toList()
                )

                messageList.add(MessageModal(question, "user"))
                messageList.add(MessageModal("Typing....", "model"))

                val response = chat.sendMessage(question)

                messageList.removeAt(messageList.lastIndex)
                messageList.add(MessageModal(response.text.toString(), "model"))
            } catch (e: Exception) {
                messageList.removeAt(messageList.lastIndex)
                messageList.add(MessageModal("Error : " + e.message.toString(), "model"))
            }
        }
    }
}