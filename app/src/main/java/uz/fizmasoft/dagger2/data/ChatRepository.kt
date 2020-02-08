package uz.fizmasoft.dagger2.data

import uz.fizmasoft.dagger2.data.local.ChatDAO
import uz.fizmasoft.dagger2.data.model.Message
import javax.inject.Inject

class ChatRepository @Inject constructor(private val chatDAO: ChatDAO) {
    fun insertMessage(message: Message) = chatDAO.insertMessage(message)
    fun fetchAllMessage() = chatDAO.getAllMessages()
}