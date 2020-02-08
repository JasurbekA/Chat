package uz.fizmasoft.dagger2

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import uz.fizmasoft.dagger2.di.DaggerAppComponent

class BaseApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.builder().application(this).build()

}
