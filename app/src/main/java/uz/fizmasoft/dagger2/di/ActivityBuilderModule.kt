package uz.fizmasoft.dagger2.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import uz.fizmasoft.dagger2.ui.ChatActivity


@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(
        modules = [ChatActivityModule::class]
    )
    abstract fun injectChatActivity() : ChatActivity
}
