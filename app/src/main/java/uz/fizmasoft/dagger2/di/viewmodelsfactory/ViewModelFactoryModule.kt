package uz.fizmasoft.dagger2.di.viewmodelsfactory

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModuleFactory(providerFactory: ViewModelProviderFactory): ViewModelProvider.Factory

}
