package uz.fizmasoft.dagger2.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Observable
import io.reactivex.Single
import uz.fizmasoft.dagger2.data.model.Message


@Dao
interface ChatDAO {

    @Insert
    fun insertMessage(message : Message) : Single<Long>

    @Query("Select * from messages")
    fun getAllMessages() : Observable<List<Message>>
//    Note Maybe and Single should not be used here, since we observe entity tuple on change

    @Query("Delete from messages")
    fun deleteAllMessages()
}
