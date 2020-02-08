package uz.fizmasoft.dagger2.di

import android.os.Environment
import androidx.lifecycle.ViewModel
import com.obsez.android.lib.filechooser.ChooserDialog
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import uz.fizmasoft.dagger2.di.keys.ViewModelKey
import uz.fizmasoft.dagger2.ui.ChatActivity
import uz.fizmasoft.dagger2.ui.ChatViewModel


@Module
abstract class ChatActivityModule {
    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    abstract fun bindAuthViewModel(viewModel: ChatViewModel): ViewModel

    companion object {
        @Provides
        @JvmStatic
        fun provideFileChooser(chatActivity: ChatActivity): ChooserDialog =
            ChooserDialog(chatActivity).withStartFile(Environment.MEDIA_MOUNTED_READ_ONLY)
    }

}
