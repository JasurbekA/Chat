package uz.fizmasoft.dagger2.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import uz.fizmasoft.dagger2.data.local.AppDataBase
import uz.fizmasoft.dagger2.data.local.ChatDAO
import javax.inject.Singleton

const val DB_NAME = "ChatAppDatabase.db"

@Module
class AppModule {
    // here we can declare app level dependencies

    @Singleton
    @Provides
    fun provideAppDatabase(application: Application): AppDataBase =
        Room.databaseBuilder(application, AppDataBase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()


    @Singleton
    @Provides
    fun provideShowDao(appDataBase: AppDataBase): ChatDAO = appDataBase.chatDao()

}
