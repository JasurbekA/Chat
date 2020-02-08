package uz.fizmasoft.dagger2.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.fizmasoft.dagger2.data.model.Message

const val DB_VERSION = 2


@Database(entities = [Message::class], version = DB_VERSION, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun chatDao(): ChatDAO
}
