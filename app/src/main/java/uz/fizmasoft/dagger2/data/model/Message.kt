package uz.fizmasoft.dagger2.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat
import java.util.*

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val sendDate: String = DateFormat.getDateTimeInstance().format(Date()),

    @ColumnInfo(name = "sender_id")
    val senderID: Long,

    @ColumnInfo(name = "recipient_id")
    val recipientID: Long,

    @ColumnInfo(name = "msg_text")
    val messageContent: String,

    @ColumnInfo(name = "file_path")
    val filePath: String? = null
)
